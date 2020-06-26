package BancoDados;
import acessoimport.ViewAFacil;
import acessoimport.cutImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import javax.swing.JOptionPane;
public class DAOControler {
    public Connection conFirebird = null;
    public Connection conSQLite;
    public Connection conSQLiteFotos;
    public Statement stmFirebird;  
    public Statement stmSQLite;   
    public Statement stmFotos;
    public Statement stm2Firebird;
    public boolean bdAcesso  = false;
    public boolean bdGuarita = false;
    public boolean bdFotos   = false;
    public void msgLog(String v){
        ViewAFacil msg = new ViewAFacil();
        msg.MostraLog(v);
    }
    public boolean ConexaoFirebird(String Diretorio, int versao) {
        Diretorio = changeChar(Diretorio,"\\","//");
        String arquivo;
        Properties props = new Properties();
        props.setProperty("user", "SYSDBA");
        props.setProperty("password", "masterkey");
        props.setProperty("encoding", "NONE");
        if(versao==1){arquivo="ACESSO.GDB";}else{arquivo="ACESSO_NG.GDB";}
        try {
            Class.forName("org.firebirdsql.jdbc.FBDriver");
            conFirebird = DriverManager.getConnection(
               "jdbc:firebirdsql:localhost/3050:"+Diretorio+"/"+arquivo,props);
            stmFirebird = conFirebird.createStatement();
            bdAcesso = true;
        } catch (ClassNotFoundException | SQLException e) {
            bdAcesso = false;
            JOptionPane.showMessageDialog(null,"Não foi possível conectar ao banco: " + e.getMessage());
        }
        return bdAcesso;
   }
    public boolean ConexaoSQLite(String Diretorio) throws IOException {
      Diretorio = changeChar(Diretorio,"\\","//");
      //copyFile(Diretorio);
      try {
        Class.forName("org.sqlite.JDBC");
        conSQLite = DriverManager.getConnection("jdbc:sqlite:"+Diretorio+"/dataBase");
        stmSQLite = conSQLite.createStatement();
        bdGuarita = true;
      } catch (ClassNotFoundException | SQLException e) {
        bdGuarita = false;
        //JOptionPane.showMessageDialog(null,"Não foi possível conectar ao banco CONTROL GUARITA: " + e.getMessage());
      }
      return bdGuarita;
   }
    public boolean ConexaoSQLiteFotos(String Diretorio) {
      Diretorio = changeChar(Diretorio,"\\","//");
      try {
        Class.forName("org.sqlite.JDBC");
        conSQLiteFotos = DriverManager.getConnection("jdbc:sqlite:"+Diretorio+"/database_fotos");
        stmFotos = conSQLiteFotos.createStatement();
        bdFotos = true;
      } catch (ClassNotFoundException | SQLException e) {
        //JOptionPane.showMessageDialog(null,"Não foi possível conectar ao banco CONTROL GUARITA: " + e.getMessage());
      }
      return bdFotos;
   } 
    public boolean desconecta(){
        boolean result = true;
        try 
        {
            conFirebird.close();
            conSQLite.close();
            conSQLiteFotos.close();
            //JOptionPane.showMessageDialog(null,"banco fechado");
        }
        catch(SQLException fecha)
        {
            JOptionPane.showMessageDialog(null,"Não foi possivel "+
                    "fechar o banco de dados: "+fecha);
            result = false;
        }
        return result;
    }  
    public boolean import_rota(int ver) throws SQLException {
        /**********************************************************************************************************
        *                       TRATAMENTO DA TABELA ROTAS
        ***********************************************************************************************************/
        boolean retorno = false;
        Statement stmt;
        PreparedStatement stsq;
        String sql, iSql, name;
        int rota;
        ResultSet rs;
        try {
            if(ver==1){
                sql  = "SELECT CODROTA,DESCRICAO_ROTA FROM ROTAS ORDER BY CODROTA" ;
            }else{
                sql = "SELECT ROT_COD_ROTA AS CODROTA, ROT_NOME_ROTA AS DESCRICAO_ROTA FROM TB_ROTAS ORDER BY ROT_COD_ROTA";
            }
            iSql = "INSERT INTO ROTAS(rota,name,isMorador,isVisitante) values(?,?,?,?)";
            stmt = conFirebird.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {    
                System.out.println(rs.getRow());
                stsq = conSQLite.prepareStatement(iSql);
                stsq.setInt(1, rs.getInt("CODROTA"));
                stsq.setString(2, rs.getString("DESCRICAO_ROTA"));
                if(rs.getString("DESCRICAO_ROTA").indexOf("MORADOR")==0){
                    stsq.setInt(3,1);
                    stsq.setInt(4,0);
                }else{
                    stsq.setInt(3,0);
                    stsq.setInt(4,1);
                }
                System.out.println(rs.getInt("CODROTA"));
                try{
                    stsq.execute();
                    stsq.close();
                } catch (SQLException | NumberFormatException ex) {
                    System.out.println(ex.toString());
                }                
            }
            retorno = true;
        } catch (SQLException | NumberFormatException ex) {
            System.out.println(ex.toString());
        }
        return retorno;
    }
    public boolean import_quadras(int versao, int Tipo) throws SQLException {
        /**********************************************************************************************************
        *                       TRATAMENTO DA TABELA QUADRAS
        *   Anotacao: Moradores que não estivem dentro de um Bloco na base Acesso nao serão importados
        *   
        ***********************************************************************************************************/
        // verificar sobre o campo label, nao está claro
        msgLog("ok QUADRAS");
        boolean retorno=false;
        Statement stmt;
        PreparedStatement stsq;
        String sql, iSql;
        int id=0;
        ResultSet rs;
        try {
            if(Tipo==1){                        //  BLOCO & APTO
                if(versao==1){
                    sql  = "SELECT BLOCO FROM MORADORES WHERE BLOCO<>' ' GROUP BY BLOCO order BY BLOCO" ;
                }else{
                    sql  = "SELECT UNI_BLOCO AS BLOCO FROM TB_UNIDADES WHERE UNI_BLOCO<>' ' GROUP BY UNI_BLOCO order BY UNI_BLOCO";
                }
            }else{                              // QUADRA & LOTE
                if(versao==1){
                    sql  = "SELECT COND_QUADRA AS BLOCO FROM MORADORES WHERE COND_QUADRA<>' ' GROUP BY COND_QUADRA order BY COND_QUADRA" ;
                }else{
                    sql  = "SELECT UNI_QUADRA_LOTE AS BLOCO FROM TB_UNIDADES WHERE UNI_BLOCO<>' ' GROUP BY UNI_QUADRA_LOTE order BY UNI_QUADRA_LOTE";
                }              
            }
                
            iSql = "INSERT INTO QUADRAS(id,descricao,label) values(?,?,?)";
            stmt = conFirebird.createStatement();
            rs = stmt.executeQuery(sql);
            String cBloco = "Bloco ";
            if(Tipo!=1){
                cBloco = "Quadra ";
            }
            
            while (rs.next()) {    
                stsq = conSQLite.prepareStatement(iSql);
                id++;
                stsq.setInt(1, id);
                stsq.setString(2, cBloco+rs.getString("BLOCO"));
                stsq.setInt(3, id-1);
                try{
                    stsq.execute();
                    stsq.close();
                } catch (SQLException | NumberFormatException ex) {
                    System.out.println(ex.toString());
                }                
            }
            retorno = true;
        } catch (SQLException | NumberFormatException ex) {
            System.out.println(ex.toString());
        }
        return retorno;
    }
    public boolean import_unidades(int versao, int Tipo) throws SQLException {
        /**********************************************************************************************************
        * 
        *                       TRATAMENTO DA TABELA UNIDADES
        *   
        ***********************************************************************************************************/
        System.out.println("*********** IMPORTANDO UNIDADES ******************************************************");
        boolean retorno = false;
        stmFirebird = conFirebird.createStatement();
        stmSQLite   = conSQLite.createStatement();
        PreparedStatement stInsert;
        String sql_Acesso_Moradores, crud_Control_Unidades, SQL, cBloco;
        ResultSet rsAcessoA, rsControl;
        int id=1;
        try {
            if(Tipo==1){   // TIPO IGUAL A APARTAMENTO/NUMERO
                cBloco ="Bloco ";
                if(versao==1){
                    sql_Acesso_Moradores = "SELECT X.*,(select FIRST 1 TELEFONE from Moradores M WHERE M.BLOCO=X.BLOCO and M.APTO=X.APTO and  CHARACTER_LENGTH(M.telefone)>0) AS INTERFONE FROM " +
                    "(SELECT BLOCO,APTO FROM MORADORES WHERE BLOCO<>' ' and APTO<>'0000' AND Upper(CONDOMINO) NOT LIKE ('%VISITANTE%') " +
                    "AND Upper(CONDOMINO) NOT LIKE ('%HOSPEDE%') GROUP BY BLOCO, APTO ORDER BY BLOCO,APTO) AS X";
                }else{
                    sql_Acesso_Moradores = "SELECT UNI_COD_UNIDADE AS ID_UNIDADE, UNI_BLOCO AS BLOCO,UNI_APTO_NUM AS APTO,CASE when CHAR_LENGTH(UNI_RAMAL) = 0 THEN UNI_TELEFONE01 " 
                                         + " else UNI_RAMAL END AS INTERFONE FROM TB_UNIDADES WHERE UNI_BLOCO<>' ' " +
                                           "ORDER BY UNI_BLOCO,UNI_APTO_NUM";
                }
            }else{         // TIPO IGUAL A LOTE/QUADRA
                cBloco ="Quadra ";
                if(versao==1){
                    sql_Acesso_Moradores = "SELECT X.*,(select FIRST 1 TELEFONE from Moradores M WHERE M.COND_QUADRA=X.BLOCO and M.APTO=X.APTO and  CHARACTER_LENGTH(M.telefone)>0) AS INTERFONE FROM\n" +
"                    (SELECT COND_QUADRA AS BLOCO,APTO FROM MORADORES WHERE COND_QUADRA<>' ' AND Upper(CONDOMINO) NOT LIKE ('%VISITANTE%') \n" +
"                    AND Upper(CONDOMINO) NOT LIKE ('%HOSPEDE%') GROUP BY BLOCO, APTO ORDER BY BLOCO,APTO) AS X";
                }else{
                    sql_Acesso_Moradores = "SELECT UNI_COD_UNIDADE AS ID_UNIDADE, UNI_QUADRA_LOTE AS BLOCO,UNI_APTO_NUM AS APTO, CASE when CHAR_LENGTH(UNI_RAMAL) = 0 THEN UNI_TELEFONE01 " 
                                         + " else UNI_RAMAL END AS INTERFONE FROM TB_UNIDADES WHERE UNI_QUADRA_LOTE<>' ' " +
                                           "ORDER BY UNI_BLOCO,UNI_APTO_NUM";
                }                
            }
            crud_Control_Unidades  = "INSERT INTO UNIDADES(id,quadra,lote,status,proprietario,telefone) values(?,?,?,?,?,?)";
            int Apto;
            rsAcessoA    = stmFirebird.executeQuery(sql_Acesso_Moradores);
            while (rsAcessoA.next()) {    
                stInsert = conSQLite.prepareStatement(crud_Control_Unidades);
                id++;
                if(versao==1){
                    stInsert.setInt(1, id);
                }else{
                    stInsert.setInt(1, rsAcessoA.getInt("ID_UNIDADE"));
                }
                /*********************************************************************
                 *   verifica se o campo APTO tem digitos de 0 a 9. Se conter
                 *   letras insere o valor 0 n campo apto
                 *   
                 *********************************************************************/
                //if(isInteger(rs.getString("APTO"))){
                //    stsq.setInt(3,rs.getInt("APTO")); 
                //}else{
                //    stsq.setInt(3,0);
                //}
                if(rsAcessoA.getString("APTO").trim().length()>0){
                    Apto = Integer.parseInt(rsAcessoA.getString("APTO").trim());
                }else{
                    Apto = 0;
                }
                stInsert.setInt(3,Apto);
                    
                stInsert.setInt(4,0); //status
                stInsert.setString(6,rsAcessoA.getString("INTERFONE"));
                /**********************************************************************
                 *  busca a quadra no banco CONTROL GUARITA, caso nao exista na base
                 *  insere com valor 0
                 **********************************************************************/
                SQL       = "SELECT ID FROM QUADRAS WHERE DESCRICAO='"+cBloco+rsAcessoA.getString("BLOCO")+"'";
                rsControl = stmSQLite.executeQuery(SQL);
                if(rsControl.next()){
                    stInsert.setInt(2,rsControl.getInt("ID"));
                }else{
                    stInsert.setInt(2,0);
                }
                rsControl.close();
                /********************************************************************************
                *   verificar se é o proprietario quem mora na unidade
                *********************************************************************************/
                //stInsert.setInt(5,isMorador(rsAcessoA.getString("BLOCO"),rsAcessoA.getString("APTO")));
              
                System.out.println("Apto: "+Apto+"  Bloco: "+rsAcessoA.getString("BLOCO"));

                try{
                    stInsert.execute();
                    //stsq.close();
                } catch (SQLException | NumberFormatException ex) {
                    System.out.println(ex.toString());
                }                
            }
            retorno = true;
        } catch (SQLException | NumberFormatException ex) {
            System.out.println(ex.toString());
        }
        return retorno;
    }
    @SuppressWarnings("empty-statement")
    public boolean import_moradores(int versao, int Tipo) throws SQLException, ParseException {
        /**********************************************************************************************************
        *
        *                       TRATAMENTO DA TABELA MORADOR
        *   
        ***********************************************************************************************************/
        boolean retorno=false;
        System.out.println("----------------------IMPORTANDO MORADORES ---------------");
        stmFirebird = conFirebird.createStatement();
        stmSQLite   = conSQLite.createStatement();
        PreparedStatement stInsert;
        String sql_Acesso_Moradores, crud_Control_Moradores, SQL, cBloco;
        ResultSet rsAcessoA, rsControl;
        long dataDeHoje=System.currentTimeMillis()/1000;  
        int Apto;
        try {
            if(Tipo==1){cBloco = "Bloco ";}else{cBloco="Quadra ";};
            if(versao==1){ 
                sql_Acesso_Moradores  =  "SELECT MORADORES.*,CARTAOMORADORES.tipomorador FROM MORADORES LEFT JOIN cartaomoradores ON "
                    +"CARTAOMORADORES.codmorador=MORADORES.codmorador WHERE MORADORES.CONDOMINO NOT LIKE '%TESTE%' AND "
                    +"MORADORES.CONDOMINO NOT LIKE '%XXX%' AND MORADORES.CONDOMINO NOT LIKE '%VISITANTE%' AND MORADORES.CONDOMINO NOT "
                    +"LIKE '%HOSPEDE%' AND MORADORES.CONDOMINO<>'' AND MORADORES.CONDOMINO<>'0' ORDER BY MORADORES.CODMORADOR";
            }else{
                if(Tipo==1){
                    sql_Acesso_Moradores = "SELECT usu_cod_usuario  AS CODMORADOR, "
                            + "                    usu_nome_usuario AS CONDOMINO, "
                            + "                    USU_RG           AS RG, "
                            + "                    USU_COD_ROTA     AS ROTA, "
                            + "                    USU_COD_UNIDADE  AS UNIDADE, " 
                            + "                    USU_CELULAR      AS TELEFONE,"
                            + "                    USU_CPF          AS CPF, "
                            + "                    USU_TIPO_USUARIO AS tipomorador, "
                            + "                    USU_TIPO_USUARIO AS PARENTESCO,"
                            + "                    USU_SEXO         AS SEXO, "
                            + "                    USU_OBS          AS OBS,"   
                            +"                     UNI_UNIDADE      AS ID_UNIDADE, "
                            + "                    UNI_BLOCO        AS BLOCO, "
                            + "                    UNI_APTO_NUM     AS APTO " +
                        "from tb_usuarios left join tb_unidades ON tb_unidades.uni_cod_unidade = tb_usuarios.usu_cod_unidade WHERE USU_TP_USUARIO='M' " +
                        "ORDER BY USU_COD_USUARIO";
                }else{
                    sql_Acesso_Moradores = "SELECT usu_cod_usuario as CODMORADOR, usu_nome_usuario AS CONDOMINO, USU_RG AS RG, USU_COD_ROTA AS ROTA, USU_COD_UNIDADE AS UNIDADE, " +
                        "USU_CELULAR AS TELEFONE,USU_CPF AS CPF, USU_TIPO_USUARIO AS tipomorador, USU_TIPO_USUARIO AS PARENTESCO,USU_SEXO AS SEXO, USU_OBS AS OBS," +    
                        "UNI_UNIDADE as ID_UNIDADE, UNI_QUADRA_LOTE AS BLOCO, UNI_APTO_NUM AS APTO " +
                        "from tb_usuarios left join tb_unidades ON tb_unidades.uni_cod_unidade = tb_usuarios.usu_cod_unidade WHERE USU_TP_USUARIO='M' " +
                        "ORDER BY USU_COD_USUARIO";
                }
            }
            crud_Control_Moradores  = "INSERT INTO MORADORES(id,nome,RG,rota,data_cadastro,telefone,unidade,parentesco,autoriza,condutor,masculino,valido,observacao,nascimento) "
                    + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            rsAcessoA  = stmFirebird.executeQuery(sql_Acesso_Moradores);
            while (rsAcessoA.next()) {    
                stInsert = conSQLite.prepareStatement(crud_Control_Moradores);
                /***********************************************************************************************************
                 * 
                 *   localiza a unidade na base do Control Guarita, caso nao encontre nao registra morador
                 *   
                 ***********************************************************************************************************/
                if (!rsAcessoA.getString("APTO").trim().isEmpty()) {
                    Apto = Integer.parseInt(rsAcessoA.getString("APTO").trim());
                } else {
                    Apto = 0;
                }  
                if(versao==1){
                    if(Tipo==1){
                        SQL = "SELECT U.ID as ID,Q.descricao as DESCRICAO,U.lote as LOTE FROM UNIDADES U LEFT JOIN QUADRAS Q ON Q.id = U.quadra WHERE Q.descricao='"+cBloco+rsAcessoA.getString("BLOCO")+"' "
                             +" AND U.lote='"+Apto+"' ";
                    }else{
                        SQL = "SELECT U.ID as ID,Q.descricao as DESCRICAO,U.lote as LOTE FROM UNIDADES U LEFT JOIN QUADRAS Q ON Q.id = U.quadra WHERE Q.descricao='"+cBloco+rsAcessoA.getString("COND_QUADRA")+"' "
                              +" AND U.lote='"+Apto+"' ";
                    }                    
                }else{
                    SQL = "SELECT U.ID as ID,Q.descricao as DESCRICAO,U.lote as LOTE FROM UNIDADES U LEFT JOIN QUADRAS Q ON Q.id = U.quadra WHERE Q.descricao='"+cBloco+rsAcessoA.getString("BLOCO")+"' "
                           +" AND U.lote='"+Apto+"' ";
                }

                System.out.println(SQL);
                rsControl = stmSQLite.executeQuery(SQL);
                if(rsControl.next()){
                    System.out.println(rsAcessoA.getString("CONDOMINO"));
                    stInsert.setInt(1,rsAcessoA.getInt("CODMORADOR"));
                    stInsert.setString(2,rsAcessoA.getString("CONDOMINO"));
                    stInsert.setString(3,rsAcessoA.getString("RG"));
                    stInsert.setInt(7,rsControl.getInt("ID"));                                                 // Codigo da Unidade
                    if(versao==1){
                        SQL = "SELECT rota, name FROM rotas WHERE name LIKE '%"+rsAcessoA.getString("ROTA")+"%'";
                        rsControl = stmSQLite.executeQuery(SQL);
                        System.out.println(rsAcessoA.getString("CONDOMINO"));
                        if(rsControl.next()){
                            stInsert.setInt(4,rsControl.getInt("rota"));
                        }else{
                            stInsert.setInt(4, 0);
                        }
                    }else{
                        stInsert.setInt(4, rsAcessoA.getInt("ROTA"));
                    }
                    stInsert.setLong(5,dataDeHoje);
                    stInsert.setString(6, rsAcessoA.getString("CPF"));
                    /*****************************************************************************************************
                     *   
                     *                VERIFICA TITULARIDADE
                     * 
                     ****************************************************************************************************/
                    if(versao==1){           //   versao antiga
                        if("C".equals(rsAcessoA.getString("tipomorador"))){
                            stInsert.setInt(8,1);
                        }else{
                            SQL = "SELECT * FROM PARENTESCOS WHERE DESCRICAO like '%"+rsAcessoA.getString("GRAU").trim()+"%'";
                            rsControl = stmSQLite.executeQuery(SQL);
                            if(rsControl.next()){
                                stInsert.setInt(8,rsControl.getInt("id"));
                            }else{
                                stInsert.setInt(8,22);                // CADASTRAR TIPO OUTROS EM DEPENDESTES
                            }
                        }  
                    }else{                  // versao 5.0
                        if("PROPRIETARIO".equals(rsAcessoA.getString("tipomorador"))){
                            stInsert.setInt(8, 1 );
                        }else{
                            stInsert.setInt(8, 22);
                        }
                    }
                    /****************************************************************************************************/
                    stInsert.setInt(9, 1);
                    stInsert.setInt(10, 0);
                    if("M".equals(rsAcessoA.getString("SEXO"))){
                        stInsert.setInt(11, 1);
                    }else{
                        stInsert.setInt(11, 0);
                    }
                    stInsert.setInt(12, 1);
                    stInsert.setString(13, rsAcessoA.getString("OBS"));
                    stInsert.setInt(14, 2457903);
                    try{
                        stInsert.execute();
                    } catch (SQLException | NumberFormatException ex) {
                        System.out.println(ex.toString());
                    }
                }
                rsControl.close();
            }
            retorno = true;
        } catch (SQLException | NumberFormatException ex) {
            System.out.println(ex.toString());
        }
        return retorno;
    }
    public boolean import_parentescos(int versao) throws SQLException, ParseException {
        /**********************************************************************************************************
        *
        *                       seta campo parentesco na tabela MORADORES
        *   
        ***********************************************************************************************************/
        boolean retorno=false;
        if(versao==1){
            stmFirebird = conFirebird.createStatement();
            stmSQLite   = conSQLite.createStatement();
            PreparedStatement stUpdate;
            String sql_Acesso_Moradores, crud_Control_Moradores, SQL;
            ResultSet rsAcessoA, rsControl;
            System.out.println("*** verificando parentesco ***");
            try {
                sql_Acesso_Moradores    =  "SELECT * FROM MORADORES  WHERE CONDOMINO NOT LIKE '%TESTE%' AND CONDOMINO NOT LIKE '%XXX%' AND CONDOMINO NOT LIKE '%VISITANTE%' "
                        +"AND CONDOMINO NOT LIKE '%HOSPEDE%' AND CONDOMINO<>'' AND CONDOMINO<>'0' ORDER BY CODMORADOR";
                crud_Control_Moradores  = "UPDATE MORADORES SET PARENTESCO=? WHERE ID=?";
                rsAcessoA    = stmFirebird.executeQuery(sql_Acesso_Moradores);
                while (rsAcessoA.next()) {    
                    stUpdate = conSQLite.prepareStatement(crud_Control_Moradores);
                    SQL = "SELECT * FROM PARENTESCOS WHERE DESCRICAO LIKE '%"+rsAcessoA.getString("GRAU").trim()+"%'";              
                    rsControl = stmSQLite.executeQuery(SQL);
                    if(rsControl.next()){
                        stUpdate.setInt(1,rsControl.getInt("id"));
                    }else{
                        stUpdate.setInt(1, 0);
                    }
                    stUpdate.setInt(2, rsAcessoA.getInt("CODMORADOR"));
                    try{
                        stUpdate.execute();
                    } catch (SQLException | NumberFormatException ex) {
                        System.out.println(ex.toString());
                    }
                    rsControl.close();
                }
                retorno = true;
            } catch (SQLException | NumberFormatException ex) {
                System.out.println(ex.toString());
            }
            }else{
                retorno = true;
            }
        return retorno;
    }
    public boolean import_acionadores(int versao) throws SQLException {
        System.out.println("*********** IMPORTANDO ACIONADORES ******************************************************");
        boolean retorno = false;
        Statement stmt, stConsulta;
        PreparedStatement stsq;
        String sql, iSql, tipo, cSql, Hexa1, Hexa2, Hexa0, cCodigo;
        ResultSet rs, rsConsulta;
        int ConvHexa, Morador, cTipo, id, Codigo, nHexa;
        long dataDeHoje=System.currentTimeMillis()/1000;  
        try {
            /**********************************************************************************************************
            *                       TRATAMENTO DA TABELA ACIONADORES
            ***********************************************************************************************************/
            if(versao==1){
                sql = "SELECT CODCARTAO,ID,CODMORADOR,TIPOACIONADOR,NUMACIONADOR,CAR.sa, CAR.sb, CAR.sc, CAR.sd from CARTAOMORADORES CARMOR " 
                      +"inner JOIN CARTOES CAR ON CARMOR.numacionador = CAR.numcartao ORDER BY ID" ;
            }else{
                sql = "SELECT ACI_COD_ACIONADOR AS ID, ACI_COD_USUARIO AS CODMORADOR, ACI_NUMERO_ACIONADOR AS NUMACIONADOR,"
                     +"ACI_TIPO_ACIONADOR AS TIPOACIONADOR, ACI_HEXA_ACIONADOR AS HEXA FROM tb_acionadores ORDER BY ID";
            }
            iSql = "INSERT INTO ACIONADORES(id,tipo,codigo,usenha,morador,dataCriacao,enviado,rota) values(?,?,?,?,?,?,?,?)";
            stmt = conFirebird.createStatement();
            stConsulta = conSQLite.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                /*********************************************************************************************************
                 * 
                 *      PROCURAR MORADOR NA BASE DE DADOS CONTROL GUARITA
                 * 
                 *********************************************************************************************************/
                cSql ="SELECT * FROM MORADORES WHERE ID="+rs.getInt("CODMORADOR");
                rsConsulta = stConsulta.executeQuery(cSql);
                // só registra o cartão se o morador existir
                if(rsConsulta.next()){
                    stsq = conSQLite.prepareStatement(iSql);
                    id   = rs.getInt("ID");
                    tipo = rs.getString("TIPOACIONADOR");
                    Morador = rs.getInt("CODMORADOR");
                    /********************************************************************************************************
                    *     Converter HEXA 
                    *********************************************************************************************************/
                    System.out.println(rs.getInt("ID")+" tipo : " +tipo + "  Codigo: "+ rs.getString("NUMACIONADOR") + " Data: "+dataDeHoje);  
                    if(versao==1){
                        System.out.println("Codigo Acesso Facil: "+rs.getString("CODCARTAO"));
                    }
                    System.out.println("iniciando conversão");
                    if(versao==1){
                        Hexa0 = rs.getString("SA").trim();
                        Hexa1 = rs.getString("SB");
                        Hexa2 = rs.getString("SC")+rs.getString("SD");
                    }else{
                        cCodigo = rs.getString("HEXA").trim();
                        while(cCodigo.length()<7){
                           cCodigo = '0'+cCodigo;
                        }
                        System.out.println("Tamanho: "+cCodigo.length());
                        Hexa0    = cCodigo.substring(0, 1);
                        Hexa1    = cCodigo.substring(1, 3);
                        Hexa2    = cCodigo.substring(3, 7);   
                        System.out.println("HEXA_0: "+Hexa0+ "  HEXA_1: "+Hexa1+" HEXA_2: "+Hexa2+" Hexa total: "+rs.getString("HEXA").trim()+" cCodigo: "+cCodigo);
                    }
                    try{
                        nHexa    = Integer.parseInt(Hexa0);
                        ConvHexa = Integer.parseInt(Hexa1,16);             // converte os dois primeiros digitos em hexa para int
                        Codigo   = ConvHexa * 65536;                       // multiplica o resultado por 65536
                        ConvHexa = Integer.parseInt(Hexa2,16);
                        Codigo   = Codigo + ConvHexa;
                        if(!"CAR".equals(tipo) && nHexa>0 ){     
                            nHexa    = nHexa*16777216;
                            nHexa    = nHexa+Codigo;
                            Codigo   = nHexa;
                        }
                        System.out.println("Codigo Convertido  : "+Codigo);
                        if(null==tipo){
                            cTipo = 0;
                        }else switch (tipo) {
                            case "CON":
                                cTipo = 1;
                                break;                                                  ///////////////////////////////////////////////////////
                            case "TAG":                                                 // VERIFICAR SE EXISTE TAG ATIVO ANTES DE PROCESSAR //
                                cTipo = 6;                                              //////////////////////////////////////////////////////
                                break;
                            case "CAR":
                                cTipo = 3;
                                break;
                            case "TAP":
                                cTipo = 6;
                                break;
                            default:
                                cTipo = 0;
                                break;
                        }
                        stsq.setInt(1, id);
                        stsq.setInt(2, cTipo);
                        stsq.setInt(3, Codigo);
                        stsq.setInt(4, 0);
                        stsq.setInt(5, Morador);
                        stsq.setLong(6, dataDeHoje);
                        stsq.setInt(7, 0);
                        stsq.setInt(8, -1);                              // ROTA

                        try{
                            stsq.execute();
                        } catch (SQLException | NumberFormatException ex) {
                            System.out.println(ex.toString());
                        }
                    }catch (NumberFormatException ex) {
                        System.out.println("ERRO NA CONVERSAO");
                    }
                }
            }
            retorno = true;
        } catch (SQLException | NumberFormatException ex) {
            System.out.println(ex.toString());
        }
        return retorno;
    }
    public boolean import_biometria(int versao) throws SQLException {
        System.out.println("*********** IMPORTANDO BIOMETRIAS ******************************************************");
        boolean retorno=false;
        Statement stmt,st;
        PreparedStatement stsq, stsi;
        String sql, iSql, eSql;
        ResultSet rs,rt; 
        int quantidade_biometria=0;
        long dataDeHoje=System.currentTimeMillis()/1000;
        try {
            /**********************************************************************************************************
            *                       TRATAMENTO DA TABELA BIOMETRIA
            ***********************************************************************************************************/
            /* VERIFICAR SE EXISTE BIOMETRIA */  
            if(versao==1){
                sql = "select * from biometria";
            }else{
                sql = "select * from tb_biometrias";
            }
            stmt       = conFirebird.createStatement();
            rs         = stmt.executeQuery(sql);
            if(!rs.next()){
                return true;
            }
            rs.close();
            if(versao==1){
                sql  = "select biometria.cod_biometria, biometria.cod_morador, biometria.template_hexa, biometria.datacadastro, biometria.id_bio, " +
                       "biometria.enviada, biometria.cartao, biometria.cartao_wiegand, biometria.senha, biometria.adm from biometria"; 
            }else{
                sql  = "select bio_cod_biometria as cod_biometria,bio_cod_biometria as id_bio, bio_cod_usuario as cod_morador, bio_template1 as template_hexa from tb_biometrias ";
            }
            iSql = "INSERT INTO BIOMETRIA2(id,template,idBio,enviado,idCartao,idSenha,isAdmin) values(?,?,?,?,?,?,?)";
            eSql = "INSERT INTO ACIONADORES(id,tipo,codigo,usenha,morador,enviado,dataCriacao) values((SELECT MAX(ID) FROM ACIONADORES)+1,?,?,?,?,?,?)";
            stmt       = conFirebird.createStatement();
            rs         = stmt.executeQuery(sql);
            while (rs.next()) {
                stsq = conSQLite.prepareStatement(iSql);
                stsi = conSQLite.prepareStatement(eSql);
                stsq.setInt(1, rs.getInt("cod_biometria"));
                stsq.setBytes(2,hexStringToByteArray(rs.getString("template_hexa")));
                stsq.setInt(3, rs.getInt("id_bio"));
                if(versao==1){                   
                    stsq.setInt(4, rs.getInt("enviada"));
                    stsq.setInt(7, rs.getInt("adm"));
                }else{                
                    stsq.setInt(4, 0);
                    stsq.setInt(7, 0);
                }
                stsq.setInt(5, -1);                                                              // idCartao
                stsq.setInt(6, -1);                                                              // idSenha                
                /******************************************************************************************
                 * 
                 *               criando acionadores para biometria
                 * 
                 *****************************************************************************************/ 
                stsi.setInt(1, 5);
                stsi.setInt(2, rs.getInt("id_bio"));
                stsi.setInt(3, 0);
                stsi.setInt(4, rs.getInt("cod_morador"));
                stsi.setInt(5, 0);
                stsi.setLong(6, dataDeHoje);
                System.out.println("Importando Biometria: " + rs.getInt("cod_biometria")); 
                quantidade_biometria ++;
                try{
                    stsq.execute();
                    stsi.execute();
                } catch (SQLException | NumberFormatException ex) {
                    System.out.println(ex.toString());
                }
            }
            retorno = true;
        } catch (SQLException | NumberFormatException ex) {
            System.out.println(ex.toString());
        }
        if(quantidade_biometria>0){
            /**************************************************************************************************
             *         VINCULAR BIOMETRIA AO CARTAO E EXCLUIR BIOMETRIA SEM MORADOR
             */
            eSql = "delete from acionadores WHERE not EXISTS (SELECT * FROM moradores  WHERE moradores.id = acionadores.morador);";
            stsi = conSQLite.prepareStatement(eSql);
            stsi.execute();
            eSql = "SELECT * FROM BIOMETRIA2 BIO INNER JOIN ACIONADORES AC ON AC.CODIGO = BIO.idBio AND TIPO=5 ";
            stmt = conSQLite.createStatement();
            st   = conSQLite.createStatement();
            rs   = st.executeQuery(eSql);
            int valor;
            while (rs.next()) {
                eSql = "select * from acionadores where morador=" +rs.getString("morador") + " and tipo=3 order by id";
                rt = stmt.executeQuery(eSql);
                valor = 0;
                while (rt.next()){
                    valor++;
                    if(valor==1){
                        iSql = "UPDATE BIOMETRIA2 SET idCartao="+rt.getString("id")+" WHERE ID="+rs.getString("id");
                        stsq = conSQLite.prepareStatement(iSql);
                        stsq.execute();
                    }
                }
            }
            eSql = "DELETE FROM BIOMETRIA2 WHERE length(template)=0 ";
            stsi = conSQLite.prepareStatement(eSql);
            stsi.execute();
        }
        return retorno;
    }
    public void inputImage(String ImageSource, int versao) throws Exception {
        /**********************************************************************************************************
        *
        *                       Converte imagens e insire Banco  
        *   
        ***********************************************************************************************************/
        System.out.println("**************************    IMPORTANDO FOTOS    ****************************************");  
        File sourceDir = new File(ImageSource);
        if(sourceDir.exists()){
            File destFolder = new File("/Acesso-dest-images/");
            destFolder.mkdir();    
            System.out.println(ImageSource);
            cutImage.main(null ,ImageSource);  
            String updateSQL = "INSERT INTO morador(id,foto,idMorador) values((SELECT MAX(ID)FROM MORADOR)+1,?,?)",Morador;
            int CodMorador;
            File[] sourceNames = sourceDir.listFiles();
            for (int i = 0; i < sourceNames.length; i++) {
                if (sourceNames[i].isFile() && !sourceNames[i].isHidden()) {
                    Morador = sourceNames[i].getName();
                    if(Morador.indexOf("m")>0 || Morador.indexOf("u")>0){                                     //    VERSAO ANTIGA DO ACESSO FACIL
                        if(versao==1){
                            Morador = Morador.substring(0,Morador.indexOf("m"));
                        }else{
                            Morador = Morador.substring(0,Morador.indexOf("u"));
                        }
                        PreparedStatement pstmt = conSQLiteFotos.prepareStatement(updateSQL);
                        CodMorador = Integer.parseInt(Morador);
                        File file =  new File(ImageSource +"\\"+ sourceNames[i].getName());
                        FileInputStream fis = new FileInputStream(file);
                        pstmt.setBinaryStream(1, fis, (int) file.length());
                        //pstmt.setBytes(1, readFile(filename));
                        pstmt.setInt(2, CodMorador);
                        try{
                            pstmt.executeUpdate();
                            System.out.println("Gravado No Banco de Dados :"+CodMorador);  
                        } catch (SQLException | NumberFormatException ex) {
                            System.out.println(ex.toString());
                        }
                    }
                }
            }
        }
    }
    static boolean isInteger(String letras)    {    
        String Sequencia = "0123456789";
        int tamanho = 0;
        for (int x=0; x < Sequencia.length(); x++){
            for (int i=0; i < letras.length(); i++){
                if(letras.charAt(i)==Sequencia.charAt(x)){
                    tamanho++;
                }
            }
        }
        return tamanho==letras.length();
    }
    public static byte[] hexStringToByteArray(String s) {
        // -----------------------------------------------------------
        // ------------- limpando espaços em branco ------------------
        // -----------------------------------------------------------
        String cbit = "",digito;  
        //System.out.println("String Inicial: "+s+"  tamanho da string:"+s.length());
        String[] strBit;
        strBit = new String[300];
        int x=0;
        for(int i=0; i< s.length(); i=i+1){
            if(s.substring(i,i+1).trim().length()>0){
                cbit=cbit.concat(s.substring(i,i+1));
            }else{
                if(x>strBit.length-1){
                    String[] strCopy = new String[strBit.length + 1 ];
                    System.arraycopy(strBit,0,strCopy, 0, x);
                    strBit = strCopy;
                }
                strBit[x]=cbit;
                cbit="";
                x++;
               // System.out.println("i: "+i+" x:"+x+"  bit:"+strBit[x-1]);
            }       
        }
        s="";
        for(int i=0; i < strBit.length; i++){
            if(strBit[i]!=null){
                cbit = strBit[i];
                if(i>3)
                if((i>7 &&i<177) || i>188){
                    if (cbit.trim().length()==0){
                        i = strBit.length+2; 
                    }else{
                        if(cbit.trim().length()==1){
                           digito="0";    
                           digito = digito.concat(cbit);
                           cbit = digito;
                        }
                        s = s.concat(cbit);
                    }
                }
            }
            
        }
        System.out.println("String Final 1: "+s);
        if(s.length()>676){
            s=s.substring(0,676);
        }
        // -----------------------------------------------------------*/
        System.out.println("String final 2: "+s);
        int len = s.length();
        byte[] data = new byte[len / 2];
        System.out.println(s+" tamanho: "+s.length());
        for (int i = 0; i < len; i += 2) {
            //System.out.println("caracter 1: "+s.charAt(i)+" caracter 2: "+s.charAt(i+1));
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    public String changeChar(String s, String v, String t){
        int pos;
        while (s.indexOf(v)>0){
           pos = s.indexOf(v);
           s=s.substring(0,pos)+t+s.substring(pos+1);
        }
        return s;
    }
    public void copyFile(String Dir) throws FileNotFoundException, IOException{
        String DirBase  = Dir.concat("/dataBase");
        String DirFotos = Dir.concat("/dataBase");
        File arquivoOrigem = new File(DirBase);
	FileReader fis = new FileReader(arquivoOrigem);
	BufferedReader bufferedReader = new BufferedReader(fis);
	StringBuilder buffer = new StringBuilder();
	String line = "";
	while ((line = bufferedReader.readLine()) != null) {
		buffer.append(line).append("\n");			
	}
	fis.close();
	bufferedReader.close();
        File arquivoDestino = new File("C:/Program Files (x86)/ControlGuarita/dataBase");
        FileWriter writer = new FileWriter(arquivoDestino);
        writer.write(buffer.toString());
        writer.flush();
        writer.close();
        //////////////////////////////////////////   BANCO DE DADOS FOTOS
        arquivoOrigem = new File(DirFotos);
	fis = new FileReader(arquivoOrigem);
	bufferedReader = new BufferedReader(fis);
	buffer = new StringBuilder();
	line = "";
	while ((line = bufferedReader.readLine()) != null) {
		buffer.append(line).append("\n");			
	}
	fis.close();
	bufferedReader.close();
        arquivoDestino = new File("C:/Program Files (x86)/ControlGuarita/database_fotos");
        writer = new FileWriter(arquivoDestino);
        writer.write(buffer.toString());
        writer.flush();
        writer.close();
    }
    public boolean import_unidades(int Versao) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
