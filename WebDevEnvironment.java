import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class WebDevEnvironment {

    public static void main(String[] args) {
        // Create the main application window
        SwingUtilities.invokeLater(() -> new WebDevEnvironment().createAndShowGUI());
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Web Developer Environment");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Create a tabbed pane for multiple tools
        JTabbedPane tabbedPane = new JTabbedPane();

        // Add tabs for different tools
        tabbedPane.addTab("Text Editor", createTextEditorTab());
        tabbedPane.addTab("File Explorer", createFileExplorerTab());
        tabbedPane.addTab("Terminal", createTerminalTab());
        tabbedPane.addTab("Web Preview", createWebPreviewTab());

        frame.add(tabbedPane);
        frame.setVisible(true);
    }

    // Tab 1: Text Editor
    private JPanel createTextEditorTab() {
        JPanel panel = new JPanel(new BorderLayout());

        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(textArea.getText());
                    JOptionPane.showMessageDialog(null, "File saved successfully!");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error saving file: " + ex.getMessage());
                }
            }
        });

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(saveButton, BorderLayout.SOUTH);

        return panel;
    }

    // Tab 2: File Explorer
    private JPanel createFileExplorerTab() {
        JPanel panel = new JPanel(new BorderLayout());

        JList<String> fileList = new JList<>();
        JScrollPane scrollPane = new JScrollPane(fileList);

        JTextField pathField = new JTextField(System.getProperty("user.home"));
        JButton loadButton = new JButton("Load Files");
        loadButton.addActionListener(e -> {
            File dir = new File(pathField.getText());
            if (dir.isDirectory()) {
                String[] files = dir.list();
                if (files != null) {
                    fileList.setListData(files);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid directory path!");
            }
        });

        panel.add(pathField, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(loadButton, BorderLayout.SOUTH);

        return panel;
    }

    // Tab 3: Terminal
    private JPanel createTerminalTab() {
        JPanel panel = new JPanel(new BorderLayout());

        JTextArea terminalOutput = new JTextArea();
        terminalOutput.setEditable(false);

        JTextField commandInput = new JTextField();
        commandInput.addActionListener(e -> {
            String command = commandInput.getText();
            commandInput.setText("");
            try {
                Process process = Runtime.getRuntime().exec(command);
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    terminalOutput.append(line + "\n");
                }
            } catch (IOException ex) {
                terminalOutput.append("Error executing command: " + ex.getMessage() + "\n");
            }
        });

        panel.add(new JScrollPane(terminalOutput), BorderLayout.CENTER);
        panel.add(commandInput, BorderLayout.SOUTH);

        return panel;
    }

    // Tab 4: Web Preview
    private JPanel createWebPreviewTab() {
        JPanel panel = new JPanel(new BorderLayout());

        JTextField urlField = new JTextField("https://");
        JButton loadButton = new JButton("Load");
        JEditorPane webPreview = new JEditorPane();
        webPreview.setEditable(false);

        loadButton.addActionListener(e -> {
            try {
                webPreview.setPage(new URL(urlField.getText()));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error loading URL: " + ex.getMessage());
            }
        });

        panel.add(urlField, BorderLayout.NORTH);
        panel.add(new JScrollPane(webPreview), BorderLayout.CENTER);
        panel.add(loadButton, BorderLayout.SOUTH);

        return panel;
    }
}
