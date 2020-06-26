package acessoimport;
import BancoDados.DAOControler;
import java.awt.Color;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class ViewAFacil extends javax.swing.JFrame {
    public DAOControler conecta = new DAOControler();
    public int Versao=9;
    public int TipoIdentificacao=9;
    public int sucesso=0;
    static void Mensagem(String mensagem) {
        String threadName =
        Thread.currentThread().getName();
        System.out.format("%s: %s%n",threadName, mensagem);
    }
    public ViewAFacil() {
        initComponents();
        //setIconImage( new ImageIcon("C:\\Users\\vande\\Google Drive\\TRABALHO\\FIRSTCONTROL\\Projetos\\logo_guarita.ico") );
        setIconFirstControl("fundo.png");
        txtUser.setText("Copie os aquivos do Banco de dados do ACESSO FÁCIL\n e do CONTROL GUARITA para o mesmo diretorio.");
        btVersaoA.setSelected(false);
        btVersaoB.setSelected(false);
        btApto.setSelected(false);
        btQuadra.setSelected(false);
        btTeste.setVisible(false);
        Directory.setVisible(false);
        DirectoryImage.setVisible(false);
        File dirDataBase = new File("C:\\Acesso" );
        File dirImage    = new File("C:\\Acesso-source-images");
        Directory.setCurrentDirectory(dirDataBase);
        DirectoryImage.setCurrentDirectory(dirImage);
        DataBaseSource.setText("C:\\Acesso");
        ImageSource.setText("C:\\Acesso-source-images");
        btnProcess.setVisible(false);
        btnProcess1.setVisible(false);
        btnCancel.setVisible(false);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        jDialog1 = new javax.swing.JDialog();
        Directory = new javax.swing.JFileChooser();
        jLabel2 = new javax.swing.JLabel();
        btnDirImage = new java.awt.Button();
        DataBaseSource = new java.awt.TextField();
        ImageSource = new java.awt.TextField();
        jLabel1 = new javax.swing.JLabel();
        btnDirDataBase = new java.awt.Button();
        DirectoryImage = new javax.swing.JFileChooser();
        btVersaoA = new javax.swing.JRadioButton();
        btVersaoB = new javax.swing.JRadioButton();
        btnTestBD = new javax.swing.JButton();
        btnProcess = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btTeste = new javax.swing.JButton();
        ScrollPainel = new javax.swing.JScrollPane();
        txtUser = new javax.swing.JTextArea();
        btQuadra = new javax.swing.JRadioButton();
        btApto = new javax.swing.JRadioButton();
        btnProcess1 = new javax.swing.JButton();

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("IMPORTAÇÃO DE DADOS ACESSOFACIL");
        setBackground(new java.awt.Color(102, 204, 255));
        setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        setIconImages(getIconImages());
        setMaximumSize(new java.awt.Dimension(2145, 2147));

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${undecorated}=true"), this, org.jdesktop.beansbinding.BeanProperty.create("undecorated"));
        bindingGroup.addBinding(binding);

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Directory.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
        Directory.setOpaque(true);
        Directory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DirectoryActionPerformed(evt);
            }
        });
        getContentPane().add(Directory, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 630, -1));

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("DIRETÓRIO DE FOTOS");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 189, -1));

        btnDirImage.setLabel("Selecionar Diretório");
        btnDirImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDirImageActionPerformed(evt);
            }
        });
        getContentPane().add(btnDirImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(524, 229, -1, 28));

        DataBaseSource.setEditable(false);
        DataBaseSource.setEnabled(false);
        DataBaseSource.setName("SourceDataBase"); // NOI18N
        DataBaseSource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DataBaseSourceActionPerformed(evt);
            }
        });
        getContentPane().add(DataBaseSource, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 176, 479, 28));

        ImageSource.setEditable(false);
        ImageSource.setEnabled(false);
        ImageSource.setName("SourceDataBase"); // NOI18N
        ImageSource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ImageSourceActionPerformed(evt);
            }
        });
        getContentPane().add(ImageSource, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 229, 479, 28));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("DIRETÓRIO COM BANCOS DE DADOS");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 337, -1));

        btnDirDataBase.setLabel("Selecionar Diretório");
        btnDirDataBase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDirDataBaseActionPerformed(evt);
            }
        });
        getContentPane().add(btnDirDataBase, new org.netbeans.lib.awtextra.AbsoluteConstraints(524, 176, -1, 28));

        DirectoryImage.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
        DirectoryImage.setOpaque(true);
        DirectoryImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DirectoryImageActionPerformed(evt);
            }
        });
        getContentPane().add(DirectoryImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 630, -1));

        btVersaoA.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btVersaoA.setForeground(new java.awt.Color(255, 255, 255));
        btVersaoA.setSelected(true);
        btVersaoA.setText("Versão Antiga");
        btVersaoA.setFocusPainted(false);
        btVersaoA.setOpaque(false);
        btVersaoA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btVersaoAActionPerformed(evt);
            }
        });
        getContentPane().add(btVersaoA, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 110, -1, 30));

        btVersaoB.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btVersaoB.setForeground(new java.awt.Color(255, 255, 255));
        btVersaoB.setText("Versão 5.0");
        btVersaoB.setFocusPainted(false);
        btVersaoB.setOpaque(false);
        btVersaoB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btVersaoBActionPerformed(evt);
            }
        });
        getContentPane().add(btVersaoB, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 110, -1, 30));

        btnTestBD.setText("TESTAR BANCOS");
        btnTestBD.setFocusPainted(false);
        btnTestBD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTestBDActionPerformed(evt);
            }
        });
        getContentPane().add(btnTestBD, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 163, 30));

        btnProcess.setText("IMPORTAR TUDO");
        btnProcess.setToolTipText("");
        btnProcess.setFocusPainted(false);
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });
        getContentPane().add(btnProcess, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 163, 30));

        btnCancel.setText("CANCELAR");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        getContentPane().add(btnCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 50, 163, 30));

        btTeste.setText("TESTAR");
        btTeste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTesteActionPerformed(evt);
            }
        });
        getContentPane().add(btTeste, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 10, 163, -1));

        txtUser.setBackground(new java.awt.Color(0, 153, 255));
        txtUser.setColumns(20);
        txtUser.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtUser.setForeground(new java.awt.Color(255, 255, 255));
        txtUser.setRows(5);
        ScrollPainel.setViewportView(txtUser);

        getContentPane().add(ScrollPainel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 630, 230));

        btQuadra.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btQuadra.setForeground(new java.awt.Color(255, 255, 255));
        btQuadra.setSelected(true);
        btQuadra.setText("Quadra/Lote");
        btQuadra.setFocusPainted(false);
        btQuadra.setOpaque(false);
        btQuadra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btQuadraActionPerformed(evt);
            }
        });
        getContentPane().add(btQuadra, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 60, -1, 30));

        btApto.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btApto.setForeground(new java.awt.Color(255, 255, 255));
        btApto.setText("Bloco/Apto");
        btApto.setFocusPainted(false);
        btApto.setOpaque(false);
        btApto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAptoActionPerformed(evt);
            }
        });
        getContentPane().add(btApto, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 90, -1, 30));

        btnProcess1.setText("IMPORTAR FOTOS");
        btnProcess1.setFocusPainted(false);
        btnProcess1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcess1ActionPerformed(evt);
            }
        });
        getContentPane().add(btnProcess1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 10, 163, 30));

        bindingGroup.bind();

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    private void btnTestBDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTestBDActionPerformed
        if(Versao!=9 && TipoIdentificacao!=9){
            if (!conecta.ConexaoFirebird(DataBaseSource.getText(), Versao)){
                 JOptionPane.showMessageDialog(null,"Não foi possível conectar ao banco Firebird (ACESSO FACIL) ");
            }else try {
                if(!conecta.ConexaoSQLite(DataBaseSource.getText())){
                    JOptionPane.showMessageDialog(null,"Não foi possível conectar ao banco SQLite (CONTROL GUARITA) ");
                } else {
                    if (!conecta.ConexaoSQLiteFotos(DataBaseSource.getText())){
                        JOptionPane.showMessageDialog(null,"Não foi possível conectar ao banco de fotos");
                    }else {
                        btnTestBD.setText("BANCO DE DADOS OK");
                        btnTestBD.setEnabled(false);
                        btnProcess.setVisible(true);
                        btnCancel.setVisible(true);
                        btnDirDataBase.setEnabled(false);
                        btnDirImage.setEnabled(false);
                        btVersaoA.setEnabled(false);
                        btVersaoB.setEnabled(false);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ViewAFacil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            String msg="";
            if(Versao==9){ 
                msg="SELECIONE UMA VERSÃO DO ACESSO FÁCIL";
            }else if(TipoIdentificacao==9){
                msg="SELECIONE UM TIPO DE IDENTIFICAÇÃO";
            }
            JOptionPane.showMessageDialog(null,msg);       
        }

    }//GEN-LAST:event_btnTestBDActionPerformed
    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        btnProcess.setEnabled(false);
        btnProcess1.setEnabled(false);
        Thread thread = new Thread() {
            @Override
            public void run(){
                MostraLog("");
                Processa();
            }
        };
        thread.start();
    
        
    }//GEN-LAST:event_btnProcessActionPerformed
    private void DataBaseSourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DataBaseSourceActionPerformed
        
    }//GEN-LAST:event_DataBaseSourceActionPerformed
    private void btnDirDataBaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDirDataBaseActionPerformed
        Directory.setVisible(true);
        btnTestBD.setEnabled(false);
        btnDirDataBase.setEnabled(false);
        btnDirImage.setEnabled(false);
        
        
    }//GEN-LAST:event_btnDirDataBaseActionPerformed
    private void ImageSourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ImageSourceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ImageSourceActionPerformed
    private void btnDirImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDirImageActionPerformed
        DirectoryImage.setVisible(true);
        btnTestBD.setEnabled(false);
        btnDirDataBase.setEnabled(false);
        btnDirImage.setEnabled(false);
    }//GEN-LAST:event_btnDirImageActionPerformed
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
            btnTestBD.setText("TESTAR BANCOS");
            btnTestBD.setEnabled(true);
            btnProcess.setVisible(false);
            btnProcess1.setVisible(true);
            btnCancel.setVisible(false);
            conecta.desconecta();
            btVersaoA.setEnabled(true);
            btVersaoB.setEnabled(true);
            btnDirDataBase.setEnabled(true);
            btnDirImage.setEnabled(true);
    }//GEN-LAST:event_btnCancelActionPerformed
    private void DirectoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DirectoryActionPerformed
        if ("ApproveSelection".equals(evt.getActionCommand())){
            DataBaseSource.setText(Directory.getSelectedFile().getPath());
        }
        Directory.setVisible(false);
        btnTestBD.setEnabled(true);
        btnDirDataBase.setEnabled(true);
        btnDirImage.setEnabled(true);
    }//GEN-LAST:event_DirectoryActionPerformed
    private void DirectoryImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DirectoryImageActionPerformed
        if ("ApproveSelection".equals(evt.getActionCommand())){
            ImageSource.setText(DirectoryImage.getSelectedFile().getPath());
        }
        DirectoryImage.setVisible(false);
        btnTestBD.setEnabled(true);
        btnDirDataBase.setEnabled(true);
        btnDirImage.setEnabled(true);
    }//GEN-LAST:event_DirectoryImageActionPerformed
    private void btTesteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTesteActionPerformed
        if (!conecta.ConexaoFirebird(DataBaseSource.getText(), Versao)){
            JOptionPane.showMessageDialog(null,"Não foi possível conectar ao banco Firebird (ACESSO FACIL) ");
        }else try {
            if(!conecta.ConexaoSQLite(DataBaseSource.getText())){
                JOptionPane.showMessageDialog(null,"Não foi possível conectar ao banco SQLite (CONTROL GUARITA) ");
            } else {
                if (!conecta.ConexaoSQLiteFotos(DataBaseSource.getText())){
                    JOptionPane.showMessageDialog(null,"Não foi possível conectaer ao banco de fotos");
                }else {
                    //boolean ret=conecta.import_acionadores(Versao);
                    //boolean ret=conecta.tb_rota(Versao);
                    //ret=conecta.tb_quadras(Versao);
                    //boolean tb_biometria = conecta.tb_biometria();
                    boolean ret = conecta.import_unidades(Versao);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ViewAFacil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btTesteActionPerformed
    private void btVersaoAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btVersaoAActionPerformed
        if(btVersaoA.isSelected()){
            btVersaoB.setSelected(false);
            Versao = 1;
            trocaFundo("fundo.png");
        }else{
            btVersaoB.setSelected(true);
            Versao = 2;
            trocaFundo("fundo2.jpg");
        }
    }//GEN-LAST:event_btVersaoAActionPerformed
    private void btVersaoBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btVersaoBActionPerformed
        if(btVersaoB.isSelected()){
            btVersaoA.setSelected(false);
            Versao = 2;
            trocaFundo("fundo2.jpg");
        }else{
            btVersaoA.setSelected(true);
            Versao = 1;
            trocaFundo("fundo.png");
        }
    }//GEN-LAST:event_btVersaoBActionPerformed
    private void btQuadraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btQuadraActionPerformed
         if(btQuadra.isSelected()){
            btApto.setSelected(false);
            TipoIdentificacao = 2;
        }else{
            btApto.setSelected(true);
        }
    }//GEN-LAST:event_btQuadraActionPerformed
    private void btAptoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAptoActionPerformed
        if(btApto.isSelected()){
            btQuadra.setSelected(false);
            TipoIdentificacao = 1;
        }else{
            btQuadra.setSelected(true);
        }
    }//GEN-LAST:event_btAptoActionPerformed

    private void btnProcess1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcess1ActionPerformed
        MostraTxt(" ok\n\n VERIFICANDO IMAGENS");
        try {
            conecta.inputImage(ImageSource.getText(), Versao);
        } catch (Exception ex) {
            Logger.getLogger(ViewAFacil.class.getName()).log(Level.SEVERE, null, ex);
        }

        MostraTxt("\n\n\n  Processo Concluido!");
    }//GEN-LAST:event_btnProcess1ActionPerformed
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ViewAFacil().setVisible(true);
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.TextField DataBaseSource;
    private javax.swing.JFileChooser Directory;
    private javax.swing.JFileChooser DirectoryImage;
    private java.awt.TextField ImageSource;
    private javax.swing.JScrollPane ScrollPainel;
    private javax.swing.JRadioButton btApto;
    private javax.swing.JRadioButton btQuadra;
    private javax.swing.JButton btTeste;
    private javax.swing.JRadioButton btVersaoA;
    private javax.swing.JRadioButton btVersaoB;
    private javax.swing.JButton btnCancel;
    private java.awt.Button btnDirDataBase;
    private java.awt.Button btnDirImage;
    private javax.swing.JButton btnProcess;
    private javax.swing.JButton btnProcess1;
    private javax.swing.JButton btnTestBD;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    public javax.swing.JTextArea txtUser;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
    private void setIconFirstControl(String fundo) {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("logo_guarita.png")));
        ((JPanel)getContentPane()).setOpaque(false);
        ImageIcon uno=new ImageIcon(this.getClass().getResource(fundo));
        JLabel fondo= new JLabel();
        fondo.setIcon(uno);
        getLayeredPane().add(fondo,JLayeredPane.FRAME_CONTENT_LAYER);
        fondo.setBounds(0,0,uno.getIconWidth(),uno.getIconHeight());
  
    }
    private void trocaFundo(String fundo) {
        
        ImageIcon uno=new ImageIcon(this.getClass().getResource(fundo));
        JLabel fondo= new JLabel();
        fondo.setIcon(uno);
        getLayeredPane().add(fondo,JLayeredPane.FRAME_CONTENT_LAYER);
        fondo.setBounds(0,0,uno.getIconWidth(),uno.getIconHeight());
  
    }
    public void MostraTxt(String msg){
        txtUser.append(msg);
        txtUser.setCaretPosition(txtUser.getText().length());
    }
    public void MostraLog(String msg){
        SwingWorker worker;
        worker = new SwingWorker() {
            @Override
            protected Void doInBackground() throws Exception {
                // Simulate doing something useful.
                txtUser.append("\n"+msg);
                return null;
            }
        };
        worker.execute();
    }
    private void Processa(){
        MostraTxt("\n\nAguarde.....Processo iniciado");
        String erro="";
        btnProcess.setEnabled(false);
        btnProcess1.setEnabled(false);
        txtUser.repaint();
        btnCancel.setEnabled(false);
        sucesso=2;
        try {  
            MostraTxt("\n IMPORTANDO ROTAS");
            erro="ROTAS";
            if (!conecta.import_rota(Versao)){
                MostraTxt("\n   --- erro na importação de rotas ");
                JOptionPane.showMessageDialog(null,"Erro ao importar ROTAS");
                sucesso=0;
            }else{
                MostraTxt(" ok\n\n IMPORTANDO QUADRAS");
                erro="QUADRAS";
                if(!conecta.import_quadras(Versao, TipoIdentificacao)){
                    MostraTxt("\n   --- erro na importação de quadras");
                    JOptionPane.showMessageDialog(null,"Erro ao importar QUADRAS");
                    sucesso=0;
                }else{
                    erro="UNIDADES";
                    MostraTxt(" ok \n\n IMPORTANDO UNIDADES");
                    if(!conecta.import_unidades(Versao, TipoIdentificacao)){
                        MostraTxt("\n   --- erro na importação de unidades");
                        JOptionPane.showMessageDialog(null,"Erro ao importar UNIDADES");
                        sucesso=0;
                    }else{
                        erro="MORADORES";
                        MostraTxt(" ok \n\n IMPORTANDO MORADORES");
                        if(!conecta.import_moradores(Versao, TipoIdentificacao)){
                            MostraTxt("\n   --- erro na importação de moradores");
                            JOptionPane.showMessageDialog(null,"Erro ao importar MORADORES");
                            sucesso=0;
                        }else{
                            erro="PARENTESCO";
                            MostraTxt(" ok \n\n IMPORTANDO PARENTESCO");
                            if(!conecta.import_parentescos(Versao)){
                                MostraTxt("\n   --- erro na importação de parentesco"); 
                                JOptionPane.showMessageDialog(null,"Erro ao importar PARENTESCO");
                                sucesso=0;
                            }else{
                                erro="ACIONADORES";
                                MostraTxt(" ok \n\n IMPORTANDO ACIONADORES");
                                if(!conecta.import_acionadores(Versao)){
                                    MostraTxt("\n   --- erro na importação de acionadores"); 
                                    JOptionPane.showMessageDialog(null,"Erro ao importar ACIONADORES");
                                    sucesso=0;
                                }else{
                                    erro="BIOMETRIAS";
                                    MostraTxt(" ok\n\n IMPORTANDO BIOMETRIAS");
                                    if(!conecta.import_biometria(Versao)){
                                        JOptionPane.showMessageDialog(null,"Erro ao importar BIOMETRIAS");
                                        sucesso=0;
                                    }else{
                                        MostraTxt(" ok\n\n VERIFICANDO IMAGENS");
                                        conecta.inputImage(ImageSource.getText(), Versao);
                                        sucesso = 1;
                                        MostraTxt("\n\n\n  Processo Concluido!");
                                    }
                                }
                            }
                        }
                                    
                    }
                }
            }            
        } catch (SQLException ex) {
            Logger.getLogger(ViewAFacil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(ViewAFacil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ViewAFacil.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(sucesso==2){
            MostraTxt("erro");
            JOptionPane.showMessageDialog(null,"Ocorreu algum erro na importação, verifique!!!");  
        }
        btnTestBD.setText("TESTAR BANCOS");
        btnProcess.setEnabled(true);
        btnProcess1.setEnabled(true);
        btnTestBD.setEnabled(true);
        btnProcess.setVisible(false);
        btnCancel.setVisible(false);
        btnDirDataBase.setEnabled(true);
        btnDirImage.setEnabled(true);    
        btVersaoA.setEnabled(true);
        btVersaoB.setEnabled(true);   

    }
}
