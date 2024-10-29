import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.StyledEditorKit;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.print.PrinterException;

public class NotepadApp extends JFrame implements ActionListener {
    private JTextArea textArea;
    private JFileChooser fileChooser;
    
    public NotepadApp() {
        setTitle("Notepad Application");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane);
        
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));

        createMenuBar();
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem newMenuItem = new JMenuItem("New");
        JMenuItem openMenuItem = new JMenuItem("Open");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem printMenuItem = new JMenuItem("Print");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        
        newMenuItem.addActionListener(this);
        openMenuItem.addActionListener(this);
        saveMenuItem.addActionListener(this);
        printMenuItem.addActionListener(this);
        exitMenuItem.addActionListener(this);

        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(printMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);

        // Edit menu
        JMenu editMenu = new JMenu("Edit");
        JMenuItem cutMenuItem = new JMenuItem("Cut");
        JMenuItem copyMenuItem = new JMenuItem("Copy");
        JMenuItem pasteMenuItem = new JMenuItem("Paste");
        
        cutMenuItem.addActionListener(this);
        copyMenuItem.addActionListener(this);
        pasteMenuItem.addActionListener(this);
        
        editMenu.add(cutMenuItem);
        editMenu.add(copyMenuItem);
        editMenu.add(pasteMenuItem);
        menuBar.add(editMenu);

        // Format menu for font customization
        JMenu formatMenu = new JMenu("Format");
        JMenuItem fontMenuItem = new JMenuItem("Font");
        fontMenuItem.addActionListener(this);
        formatMenu.add(fontMenuItem);
        menuBar.add(formatMenu);
        
        setJMenuBar(menuBar);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        switch (command) {
            case "New":
                newFile();
                break;
            case "Open":
                openFile();
                break;
            case "Save":
                saveFile();
                break;
            case "Print":
                printFile();
                break;
            case "Exit":
                System.exit(0);
                break;
            case "Cut":
                textArea.cut();
                break;
            case "Copy":
                textArea.copy();
                break;
            case "Paste":
                textArea.paste();
                break;
            case "Font":
                chooseFont();
                break;
        }
    }

    private void newFile() {
        textArea.setText("");
    }

    private void openFile() {
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                FileReader reader = new FileReader(fileChooser.getSelectedFile());
                textArea.read(reader, null);
                reader.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error opening file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveFile() {
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                FileWriter writer = new FileWriter(fileChooser.getSelectedFile());
                textArea.write(writer);
                writer.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void printFile() {
        try {
            textArea.print();
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(this, "Error printing", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void chooseFont() {
        JFontChooser fontChooser = new JFontChooser();
        int option = fontChooser.showDialog(this);
        if (option == JFontChooser.OK_OPTION) {
            Font font = fontChooser.getSelectedFont();
            textArea.setFont(font);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NotepadApp notepad = new NotepadApp();
            notepad.setVisible(true);
        });
    }
}

// Font Chooser dialog class
class JFontChooser extends JDialog {
    private Font selectedFont;
    private JComboBox<String> fontFamilyCombo;
    private JComboBox<Integer> fontSizeCombo;
    private int option;

    public static final int OK_OPTION = 0;
    public static final int CANCEL_OPTION = 1;

    public JFontChooser() {
        setTitle("Choose Font");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fonts = ge.getAvailableFontFamilyNames();

        fontFamilyCombo = new JComboBox<>(fonts);
        fontSizeCombo = new JComboBox<>(new Integer[]{8, 10, 12, 14, 16, 18, 20, 24, 28, 32, 36, 40});

        JPanel fontPanel = new JPanel();
        fontPanel.add(new JLabel("Font:"));
        fontPanel.add(fontFamilyCombo);
        fontPanel.add(new JLabel("Size:"));
        fontPanel.add(fontSizeCombo);
        
        add(fontPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        okButton.addActionListener(e -> {
            selectedFont = new Font((String) fontFamilyCombo.getSelectedItem(), Font.PLAIN, (Integer) fontSizeCombo.getSelectedItem());
            option = OK_OPTION;
            setVisible(false);
        });
        cancelButton.addActionListener(e -> {
            option = CANCEL_OPTION;
            setVisible(false);
        });

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public int showDialog(Component parent) {
        setVisible(true);
        return option;
    }

    public Font getSelectedFont() {
        return selectedFont;
    }
}
