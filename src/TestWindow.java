/**
 * Created by Daniel Bell on 8/11/2017.
 * Program is designed to take in a list of IP addresses or hostnames and will ping for the device across the current domain.
 */

import java.awt.EventQueue;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTextArea;


public class TestWindow extends JFrame {

    private String inputPath;
    private String outputPath;
    private String[] hostname;
    ArrayList<String> storedHostname = new ArrayList<String>();

    private JPanel contentPane;
    private JButton btnInputFile = new JButton("Input File");
    private JButton btnOutputFile = new JButton("Output File");
    private JButton btnGenerateReport = new JButton("Generate Report");
    private JButton btnExit;
    private JTextArea textArea = new JTextArea();
    private JScrollPane scrollPane = new JScrollPane(textArea);


    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TestWindow frame = new TestWindow();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * Create the frame.
     */
    public TestWindow() {
        btnOutputFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            }
        });
        setTitle("Connectivity Checker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 651, 599);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        JButton btnStart = new JButton("Start");
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonAction(evt);
            }
        });
        btnStart.setAlignmentX(Component.CENTER_ALIGNMENT);

        addExitButton();
        textArea.setBounds(15, 21, 408, 324);
        textArea.setEditable(false);
        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(
                gl_contentPane.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 469, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addComponent(btnStart)
                                                .addGap(18)
                                                .addComponent(btnExit))
                                        .addComponent(btnOutputFile, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnGenerateReport, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnInputFile, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE))
                                .addGap(625))
        );
        gl_contentPane.setVerticalGroup(
                gl_contentPane.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                                        .addGroup(Alignment.TRAILING, gl_contentPane.createParallelGroup(Alignment.BASELINE)
                                                .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
                                                .addComponent(btnInputFile, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addGap(42)
                                                .addComponent(btnOutputFile, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                                .addComponent(btnGenerateReport, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED, 391, Short.MAX_VALUE)
                                                .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                                                        .addComponent(btnExit)
                                                        .addComponent(btnStart))))
                                .addContainerGap())
        );
        contentPane.setLayout(gl_contentPane);

        addInputButton();
        addOutputButton();
        //Console.redirectOutput(textArea);

    }

    private void startButtonAction(java.awt.event.ActionEvent evt) {

        // Run method where command lines are performed and recorded.
        try {
            doPingCommands();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Finished pinging all devices. All results have been recorded");
    }

    /**
     * This method adds the exit button that will close the window once clicked on
     */
    private void addExitButton() {
        btnExit = new JButton("Exit");
        //Add Exit function to button
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonAction(evt);
            }
        });
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * @param evt This is the ActionEvent when addExitButton is clicked on
     */
    private void exitButtonAction(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }

    private void addInputButton() {

        btnInputFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputButtonAction(evt);
            }
        });

    }

    private void inputButtonAction(java.awt.event.ActionEvent evt) {
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new java.io.File("Libraries\\Documents"));
        fc.setDialogTitle("Select Input File");
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if (fc.showOpenDialog(btnInputFile) == JFileChooser.APPROVE_OPTION) {
            inputPath = fc.getSelectedFile().getAbsolutePath();
        }
        textArea.append("\n" + "Input file: " + inputPath + "\n");
        iterateCSV();
    }

    private void addOutputButton() {

        btnOutputFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                outputButtonAction(evt);
            }
        });

    }

    private void outputButtonAction(java.awt.event.ActionEvent evt) {
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new java.io.File("Libraries\\Documents"));
        fc.setDialogTitle("Select Output File");
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if (fc.showOpenDialog(btnOutputFile) == JFileChooser.APPROVE_OPTION) {
            outputPath = fc.getSelectedFile().getAbsolutePath();
        }
        textArea.append("\nOutput file: " + fc.getSelectedFile().getAbsolutePath() + "\n");
    }

    /*
      * This method calls a BufferedReader to read a CSV file and load it into an
      * array.
      */
    public void iterateCSV() {

        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";


        try {

            br = new BufferedReader(new FileReader(inputPath));
            if (inputPath != null) {
                while ((line = br.readLine()) != null) {

                    // use comma as separator
                    hostname = line.split(cvsSplitBy);
                    textArea.append("\n Hostname:  " + hostname[0]);
                    storedHostname.add(hostname[0]);

                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
     * This method takes the array from iterateCSV() and pings each of the
     * hostnames listed in there.
     */
    public void doPingCommands() throws IOException {
        for (String host : storedHostname) {
            DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
            Date dateLong = new Date();
            String date = dateFormat.format(dateLong);
            String status;

            try {
                InetAddress address = InetAddress.getByName(host);

                if (address.isReachable(2000) == true) {
                    status = "Up";
                    textArea.append(host + ": Online" + "\n");
                } else {
                    status = "Down";
                    textArea.append(host + ": Offline" + "\n");
                }
            } catch (Exception e) {
                status = "Down";
                textArea.append(host + ": Offline" + "\n");
            }
            writeCSV(date, host, status);
            textArea.append("Completed.\n");
        }
    }

    public void writeCSV(String date, String hostname, String status) throws IOException {
        FileWriter pw = new FileWriter(outputPath, true);
        StringBuilder sb = new StringBuilder();

        sb.append(date);
        sb.append(',');
        sb.append(hostname);
        sb.append(',');
        sb.append(status);
        sb.append('\n');

        pw.write(sb.toString());
        pw.close();
        textArea.append("done!" + "\n");
    }
}