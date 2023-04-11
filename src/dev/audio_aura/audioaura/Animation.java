package dev.audio_aura.audioaura;

import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class Animation 
{
	JFrame frmAnimate;
	ImageIcon iconAnimate, introIcon;
	JLabel lblIntro;
	
	int width = 350;
	int height = 250;
	
	public void animate()
	{
		
		frmAnimate = new JFrame();
		frmAnimate.setSize(width, height);
		frmAnimate.setUndecorated(true);
		frmAnimate.setLayout(null);
		frmAnimate.setLocationRelativeTo(null);
                frmAnimate.setResizable(true);
                frmAnimate.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		iconAnimate  = new ImageIcon("src/assets/src/assets/AudioAuraIcon.png");
		Image imageAnimateAura = iconAnimate.getImage();
		iconAnimate.setImage(imageAnimateAura);
		frmAnimate.setIconImage(imageAnimateAura);
		
		introIcon = new ImageIcon("src/assets/AudioAuraIcon.png");
		Image imgAuraAnimateIntro = introIcon.getImage();
		imgAuraAnimateIntro = imgAuraAnimateIntro.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		introIcon.setImage(imgAuraAnimateIntro);
		
		lblIntro = new JLabel(introIcon);
		lblIntro.setBounds(0, 0, width, height);
		lblIntro.setLayout(null);
		frmAnimate.getContentPane().add(lblIntro);
		frmAnimate.setBackground(Color.black);
		
		frmAnimate.setVisible(true);
		
		new Thread()
		{
                        @Override
			public void run()
			{
				try
				{
					AudioAura objAura = new AudioAura();
			
					sleep(30);
					objAura.init();
					frmAnimate.dispose();
				}
				catch (Exception e) {}
			}
		}.start();
	}//function animate() closed here
}
