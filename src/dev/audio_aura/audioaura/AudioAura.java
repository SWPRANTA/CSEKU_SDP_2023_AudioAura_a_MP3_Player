package dev.audio_aura.audioaura;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class AudioAura {

    JFrame frm;
    JPanel pnlHeader, pnlHeaderTrack, pnlBody, pnlBodyList;
    JLabel label, lblAni;
    JButton btnLogo, btnStop, btnMPrev, btnMPP, btnMNext;
    ImageIcon iconAura, iconLogo, iconReload, iconPrev, iconPlay, iconPause, iconStatic, iconNext;
    Image imageAura, imageLogo, imageReload, imagePrev, imagePlay, imagePause, imageStatic, imageNext;
    ImageIcon iconAni0;
    Image imageAni0;

    DefaultListModel<String> listModel;
    DefaultListModel<String> playListModel = new DefaultListModel<>();
    JList<String> list;
    JScrollPane scrollPane;

    long pauseLoc, songLength;
    int playstatus = 0, filepathresponse, trackNo = 0;
    //play status 0 for stop , 1 for playing, 2 for paused
    public Player player;
    FileInputStream fis1;
    File[] selectedFile;

    BufferedInputStream bis1;
    JFileChooser fcPath = new JFileChooser();
    String strPath = "", strPathNew;
    FileNameExtensionFilter filter;

    MoveMouseListener mml1, mml2, mml3, mml4;

    MarqueeLabel lblCurrentSong;

    int width = 620, height = 450;

    protected void init() {
        frm = new JFrame();
        frm.setSize(width, height);
        frm.setLocationRelativeTo(null);
        //frm.setUndecorated(true);
        frm.setLayout(null);
        frm.setOpacity(1.0f);
        frm.setResizable(false);

        frm.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                int resp = JOptionPane.showConfirmDialog(frm, "Do you want to exit AudioAura?",
                        "Exit AudioAura?", JOptionPane.YES_NO_OPTION);

                if (resp == JOptionPane.YES_OPTION) {
                    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    System.exit(0);
                } else {
                    frm.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });

        iconAura = new ImageIcon("src/assets/AudioAuraIcon.png");
        imageAura = iconAura.getImage();
        iconAura.setImage(imageAura);
        frm.setIconImage(imageAura);

        pnlHeader = new JPanel();
        pnlHeader.setBackground(Color.lightGray);
        pnlHeader.setBounds(0, 0, width, 50);
        pnlHeader.setLayout(null);
        frm.getContentPane().add(pnlHeader);

        ////////////////////////////icon_Logo starts here ////////////////////////////////////
        iconLogo = new ImageIcon("src/assets/AudioAuraIcon.png");
        imageLogo = iconLogo.getImage();
        imageLogo = imageLogo.getScaledInstance(39, 39, Image.SCALE_SMOOTH);
        iconLogo.setImage(imageLogo);
        //////////////////////////// icon_Logo closed here /////////////////////////////////////

        btnLogo = new JButton();
        btnLogo.setBounds(5, 5, 40, 40);
        btnLogo.setFont(new Font("Times New Roman", Font.BOLD, 9));
        btnLogo.setFocusPainted(false);
        btnLogo.setBorderPainted(false);
        btnLogo.setContentAreaFilled(false);
        btnLogo.setBackground(Color.black);
        btnLogo.setIcon(iconLogo);

        pnlHeader.add(btnLogo);

        label = new JLabel("AudioAura");
        label.setBounds(50, 5, width - 50, 40);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setForeground(Color.black);
        label.setFont(new Font("Bahnschrift SemiBold SemiConden", Font.ROMAN_BASELINE, 28));
        pnlHeader.add(label);

        mml1 = new MoveMouseListener(pnlHeader);
        pnlHeader.addMouseListener(mml1);
        pnlHeader.addMouseMotionListener(mml1);

        pnlHeaderTrack = new JPanel();
        pnlHeaderTrack.setBackground(Color.BLACK);
        pnlHeaderTrack.setBounds(0, 52, width, 30);
        pnlHeaderTrack.setLayout(null);
        frm.getContentPane().add(pnlHeaderTrack);

        mml4 = new MoveMouseListener(pnlHeaderTrack);
        pnlHeaderTrack.addMouseListener(mml4);
        pnlHeaderTrack.addMouseMotionListener(mml4);

        Decoration();

        frm.setVisible(true);

    }//init()_method closed here

    protected void stopPlayer() {
        try {
            playstatus = 0;
            strPath = "";
            lblCurrentSong.setText("Hit the PLAY button");
            btnMPP.setIcon(iconPlay);
            lblAni.setIcon(iconStatic);
            player.close();
            trackNo = 0;
            btnMPP.setToolTipText("Select MP3 files");
            listModel.removeAllElements();

            try {
                FileWriter fw = new FileWriter("src/assets/PlayList.txt", false);
                fw.close();
            } catch (IOException e) {
            }
        } catch (Exception e) {
        }
    }//stopPlayer()_method closed here

    protected void playSong(String path) {
        try {
            fis1 = new FileInputStream(path);
            bis1 = new BufferedInputStream(fis1);
            player = new Player(bis1);
            songLength = fis1.available();
            playstatus = 1;
            btnMPP.setIcon(iconPause);
            setVisual();
            lblCurrentSong.setText(selectedFile[trackNo].getName());
            strPathNew = path + "";
            btnMPP.setToolTipText("Pause");

        } catch (FileNotFoundException | JavaLayerException ex) {
            playstatus = 0;
            btnMPP.setIcon(iconPlay);
            lblAni.setIcon(iconLogo);
            lblCurrentSong.setText("");
            btnMPP.setToolTipText("Select MP3 files");

        } catch (IOException e) {
        }
        new Thread() {
            @Override
            public void run() {
                try {
                    player.play();

                    if (player.isComplete()) {
                        btnMNext.doClick();
                    }
                } catch (JavaLayerException e) {
                    strPath = "";
                    playstatus = 0;
                    lblCurrentSong.setText("");
                    btnMPP.setIcon(iconPlay);
                    lblAni.setIcon(iconLogo);
                }
            }
        }.start();

    }//plays method closed here

    protected void pausePlayer() {
        if (player != null) {
            try {
                pauseLoc = fis1.available();
                player.close();
                playstatus = 2;
                btnMPP.setToolTipText("Resume");
            } catch (IOException e) {
            }
        }
    }//pause method closed here

    protected void resumePlayer() {

        try {
            System.out.println("resume " + strPathNew);

            fis1 = new FileInputStream(strPathNew);
            bis1 = new BufferedInputStream(fis1);
            player = new Player(bis1);
            songLength = fis1.available();
            playstatus = 1;
            fis1.skip(songLength - pauseLoc);
            btnMPP.setIcon(iconPause);
            setVisual();
            lblCurrentSong.setText(selectedFile[trackNo].getName());
            btnMPP.setToolTipText("Pause");
        } catch (FileNotFoundException | JavaLayerException ex) {
            playstatus = 0;
            btnMPP.setIcon(iconPlay);
            lblAni.setIcon(iconLogo);
            lblCurrentSong.setText("");
            JOptionPane.showMessageDialog(null, ex);
            btnMPP.setToolTipText("Select MP3 files");
            stopPlayer();
        } catch (IOException e) {
        }
        new Thread() {
            @Override
            public void run() {
                try {
                    player.play();
                    if (player.isComplete()) {
                        btnMNext.doClick();
                    }
                } catch (JavaLayerException e) {
                    JOptionPane.showMessageDialog(null, e);
                    strPath = "";
                    playstatus = 0;
                    lblCurrentSong.setText("");
                    btnMPP.setIcon(iconPlay);
                    lblAni.setIcon(iconLogo);
                }
            }
        }.start();

    }//resume method ended here

    protected void prevTrack() {
        try {
            if (trackNo == 0) {
                trackNo = selectedFile.length;
            }

            player.close();
            trackNo--;
        } catch (Exception e2) {
        }

        if (trackNo == selectedFile.length - 1 && selectedFile.length - 1 == 0) {
            jumpTrack(selectedFile.length - 1);
        } else {
            try {
                list.setSelectedIndex(trackNo);
            } catch (Exception e) {
            }
        }
    }//prevTrack()_method closed here

    protected void playPauseTrack() {

        switch (playstatus) {
            case 0 -> {
                fcPath.setFileFilter(filter);
                fcPath.setMultiSelectionEnabled(true);
                fcPath.setCurrentDirectory(new File("C:/Users/swapn/OneDrive/Desktop/Music"));
                //fcPath.setCurrentDirectory(new File(System.getProperty("user.home")));
                filepathresponse = fcPath.showOpenDialog(pnlBody);
                if (filepathresponse == JFileChooser.APPROVE_OPTION) {
                    // user selects a file
                    selectedFile = null;
                    selectedFile = fcPath.getSelectedFiles();
                    strPath = selectedFile[0].getAbsolutePath();

                    trackNo = 0;
                    strPath = strPath.replace("\\", "\\\\");

                    for (int i = 0; i < selectedFile.length; i++) {
                        listModel.addElement(selectedFile[i].getName());
                        playListModel.addElement(selectedFile[i].getName());
                    }

                    playstatus = 1;
                    list.setSelectedIndex(0);

                }
            }
            case 1 -> {
                btnMPP.setIcon(iconPlay);
                lblAni.setIcon(iconStatic);
                playstatus = 2;
                pausePlayer();
            }
            default ->
                resumePlayer();
        }
    }//playPauseTrack()_method closed here

    protected void nextTrack() {
        try {
            if (trackNo == selectedFile.length - 1) {
                trackNo = -1;
            }

            player.close();
            trackNo++;
        } catch (Exception e2) {
        }

        if (trackNo == 0 && selectedFile.length - 1 == 0) {
            jumpTrack(trackNo);
        } else {
            try {
                list.setSelectedIndex(trackNo);
            } catch (Exception e) {
            }
        }
    }//nextTrack()_method closed here

    protected void setVisual() {
        lblAni.setIcon(iconAni0);
    }

    protected void jumpTrack(int index) {
        try {
            player.close();
            trackNo = index;
            strPath = selectedFile[trackNo].getAbsolutePath();
            strPath = strPath.replace("\\", "\\\\");

        } catch (Exception e2) {
        }
        if (filepathresponse == 0 && playstatus != 0) {
            playSong(strPath);
        }
    }//jumpTrack()_method closed here

    protected void Decoration() {
        pnlBody = new JPanel();
        pnlBody.setBackground(Color.white);
        pnlBody.setBounds(0, 84, width - 250, height - 84);
        pnlBody.setLayout(null);
        frm.getContentPane().add(pnlBody);

        mml2 = new MoveMouseListener(pnlBody);
        pnlBody.addMouseListener(mml2);
        pnlBody.addMouseMotionListener(mml2);

        lblCurrentSong = new MarqueeLabel("Hit the PLAY button", MarqueeLabel.RIGHT_TO_LEFT, 20);
        lblCurrentSong.setFont(new Font("Times New Roman", Font.BOLD, 14));
        lblCurrentSong.setForeground(new Color(127, 255, 0));
        lblCurrentSong.setBounds(05, 05, width, 20);
        lblCurrentSong.setBackground(Color.red);
        pnlHeaderTrack.add(lblCurrentSong);

        iconReload = new ImageIcon("src/assets/reload.png");
        imageReload = iconReload.getImage();
        imageReload = imageReload.getScaledInstance(39, 39, Image.SCALE_SMOOTH);
        iconReload.setImage(imageReload);

        btnStop = new JButton(iconReload);
        btnStop.setBounds(20, 270, 42, 42);
        btnStop.setFont(new Font("Times New Roman", Font.BOLD, 9));
        btnStop.setBackground(Color.BLACK);
        btnStop.setFocusPainted(false);
        btnStop.setBorderPainted(false);
        btnStop.setContentAreaFilled(false);
        btnStop.setToolTipText("Delete Playlist");
        btnStop.addActionListener((ActionEvent e) -> {
            stopPlayer();
        });
        pnlBody.add(btnStop);

        iconPrev = new ImageIcon("src/assets/previous_button.png");
        imagePrev = iconPrev.getImage();
        imagePrev = imagePrev.getScaledInstance(39, 39, Image.SCALE_SMOOTH);
        iconPrev.setImage(imagePrev);

        btnMPrev = new JButton(iconPrev);
        btnMPrev.setBounds(120, 270, 42, 42);
        btnMPrev.setFont(new Font("Times New Roman", Font.BOLD, 9));
        btnMPrev.setBackground(Color.BLACK);
        btnMPrev.setFocusPainted(false);
        btnMPrev.setBorderPainted(false);
        btnMPrev.setContentAreaFilled(false);
        btnMPrev.setToolTipText("Previous");
        btnMPrev.addActionListener((ActionEvent e) -> {
            prevTrack();
        }); //button previous-song ended here
        pnlBody.add(btnMPrev);

        iconPlay = new ImageIcon("src/assets/play_button.png");
        imagePlay = iconPlay.getImage();
        imagePlay = imagePlay.getScaledInstance(59, 59, Image.SCALE_SMOOTH);
        iconPlay.setImage(imagePlay);

        iconPause = new ImageIcon("src/assets/pause_button.png");
        imagePause = iconPause.getImage();
        imagePause = imagePause.getScaledInstance(59, 59, Image.SCALE_SMOOTH);
        iconPause.setImage(imagePause);

        btnMPP = new JButton(iconPlay);
        btnMPP.setBounds(190, 260, 63, 63);
        btnMPP.setFont(new Font("Times New Roman", Font.BOLD, 9));
        btnMPP.setBackground(Color.BLACK);
        btnMPP.setFocusPainted(false);
        btnMPP.setBorderPainted(false);
        btnMPP.setContentAreaFilled(false);

        btnMPP.setToolTipText("Select MP3 files");
        btnMPP.addActionListener((ActionEvent e) -> {
            playPauseTrack();
        });//button play-pause action ended here
        pnlBody.add(btnMPP);

        iconNext = new ImageIcon("src/assets/next_button.png");
        imageNext = iconNext.getImage();
        imageNext = imageNext.getScaledInstance(39, 39, Image.SCALE_SMOOTH);
        iconNext.setImage(imageNext);

        btnMNext = new JButton(iconNext);
        btnMNext.setBounds(280, 270, 42, 42);
        btnMNext.setFont(new Font("Times New Roman", Font.BOLD, 9));
        btnMNext.setBackground(Color.BLACK);
        btnMNext.setFocusPainted(false);
        btnMNext.setBorderPainted(false);
        btnMNext.setContentAreaFilled(false);
        btnMNext.setToolTipText("Next");
        btnMNext.addActionListener((ActionEvent e) -> {
            nextTrack();
        });
        pnlBody.add(btnMNext);

        ////////////////////////////////  Music Visualizer effect starts here ///////////////////////
        iconAni0 = new ImageIcon("src/assets/Z23b.gif");
        imageAni0 = iconAni0.getImage();
        imageAni0 = imageAni0.getScaledInstance(350, 260, Image.SCALE_DEFAULT);
        iconAni0.setImage(imageAni0);

        iconStatic = new ImageIcon("src/assets/AudioAuraIcon.png");
        imageStatic = iconStatic.getImage();
        imageStatic = imageStatic.getScaledInstance(330, 200, Image.SCALE_DEFAULT);
        iconStatic.setImage(imageStatic);

        lblAni = new JLabel(iconStatic);
        lblAni.setBounds(5, 0, 350, 260);
        pnlBody.add(lblAni);

        //////////////////////////////// Music Visualizer effect ends here ///////////////////////
        /////////////////////////////////////// pnlBodyList starts here //////////////////////////////
        pnlBodyList = new JPanel();
        pnlBodyList.setBounds(353, 84, 247, height - 84);
        pnlBodyList.setBackground(Color.black);

        TitledBorder bdrNetwork = new TitledBorder("Tracks");
        bdrNetwork.setTitleColor(new Color(0, 255, 255));
        bdrNetwork.setTitleJustification(TitledBorder.CENTER);
        bdrNetwork.setTitlePosition(TitledBorder.TOP);
        pnlBodyList.setBorder(bdrNetwork);

        mml3 = new MoveMouseListener(pnlBodyList);
        pnlBodyList.addMouseListener(mml3);
        pnlBodyList.addMouseMotionListener(mml3);

        frm.getContentPane().add(pnlBodyList);

        ////////////////////////////////////// pnlBodyList ends here  /////////////////////////////
        songList();

        filter = new FileNameExtensionFilter("MP3 File", "mp3");

    }//decoration()_method closed here

    void songList() {
        listModel = new DefaultListModel<>();

        list = new JList<>(listModel);
        list.setSize(195, height - 55);
        list.setBackground(Color.black);
        list.setForeground(new Color(102, 255, 102));

        ListSelectionListener listSelectionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if (!listSelectionEvent.getValueIsAdjusting()) {//This line prevents double events
                    jumpTrack(list.getSelectedIndex());
                    list.setSelectionBackground(new Color(255, 128, 0));
                }

            }
        };
        list.addListSelectionListener(listSelectionListener);

        scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setViewportView(list);
        scrollPane.setPreferredSize(new Dimension(240, height - 110));
        Border blackline = BorderFactory.createLineBorder(Color.black);
        scrollPane.setBorder(blackline);

        scrollPane.getViewport().getView().setBackground(Color.black);
        scrollPane.getViewport().getView().setForeground(Color.YELLOW);

        pnlBodyList.add(scrollPane);
    }

}
