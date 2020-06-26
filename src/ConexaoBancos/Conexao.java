package ConexaoBancos;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
public class Conexao {

    public Connection conFirebird = null;
    public Connection conSQLite;
    public Statement stmFirebird;  
    public Statement stmSQLite;   
    public boolean bdAcesso  = false;
    public boolean bdGuarita = false;
    public boolean ConexaoFirebird() {
        try {
            Class.forName("org.firebirdsql.jdbc.FBDriver");
            conFirebird = DriverManager.getConnection(
               "jdbc:firebirdsql:localhost/3050:C:/Acesso/ACESSO.GDB",
               "sysdba",
               "masterkey");
            stmFirebird = conFirebird.createStatement();
            bdAcesso = true;
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null,"Não foi possível conectar ao banco: " + e.getMessage());
        }
        return bdAcesso;
   }
    public boolean ConexaoSQLite() {
      try {

        Class.forName("org.sqlite.JDBC");
        conSQLite = DriverManager.getConnection("jdbc:sqlite:C:/Acesso/dataBase");
        stmSQLite = conSQLite.createStatement();
        bdGuarita = true;
      } catch (ClassNotFoundException | SQLException e) {
        JOptionPane.showMessageDialog(null,"Não foi possível conectar ao banco CONTROL GUARITA: " + e.getMessage());
      }
      return bdGuarita;
   }
    public boolean desconecta(){
        boolean result = true;
        try 
        {
            conFirebird.close();
            JOptionPane.showMessageDialog(null,"banco Firebird fechado");
        }
        catch(SQLException fecha)
        {
            JOptionPane.showMessageDialog(null,"Não foi possivel "+
                    "fechar o banco de dados: "+fecha);
            result = false;
        }
        return result;
    }  
    public void tb_rota() throws SQLException {
        /**********************************************************************************************************
        *                       TRATAMENTO DA TABELA ROTAS
        ***********************************************************************************************************/
        Statement stmt;
        PreparedStatement stsq;
        String sql, iSql, name;
        int rota;
        ResultSet rs;
        try {
            sql  = "SELECT CODROTA,DESCRICAO_ROTA FROM ROTAS ORDER BY CODROTA" ;
            iSql = "INSERT INTO ROTAS(rota,name,isMorador,isVisitante) values(?,?,?,?)";
            stmt = conFirebird.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {    
                System.out.println(rs.getRow());
                stsq = conSQLite.prepareStatement(iSql);
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
        } catch (SQLException | NumberFormatException ex) {
            System.out.println(ex.toString());
        }
    }
    public void tb_quadras() throws SQLException {
        /**********************************************************************************************************
        *                       TRATAMENTO DA TABELA QUADRAS
        *   Anotacao: Moradores que não estivem dentro de um Bloco na base Acesso nao serão importados
        *   
        ***********************************************************************************************************/
        // verificar sobre o campo label, nao está claro
        Statement stmt;
        PreparedStatement stsq;
        String sql, iSql;
        int id=0;
        ResultSet rs;
        try {
            sql  = "SELECT BLOCO FROM MORADORES WHERE BLOCO<>' ' GROUP BY BLOCO order BY BLOCO" ;
            iSql = "INSERT INTO QUADRAS(id,descricao,label) values(?,?,?)";
            stmt = conFirebird.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {    
                stsq = conSQLite.prepareStatement(iSql);
                id++;
                stsq.setInt(1, id);
                stsq.setString(2, rs.getString("BLOCO"));
                stsq.setInt(3, id-1);
                try{
                    stsq.execute();
                    stsq.close();
                } catch (SQLException | NumberFormatException ex) {
                    System.out.println(ex.toString());
                }                
            }
        } catch (SQLException | NumberFormatException ex) {
            System.out.println(ex.toString());
        }
    }
    public void tb_unidades() throws SQLException {
        /**********************************************************************************************************
        *                       TRATAMENTO DA TABELA UNIDADES
        *   
        ***********************************************************************************************************/
        stmFirebird = conFirebird.createStatement();
        stmSQLite   = conSQLite.createStatement();
        PreparedStatement stInsert;
        String sql_Acesso_Moradores, crud_Control_Unidades, sql_Busca_Proprietario, SQL;
        int id=1;
        ResultSet rsAcessoA, rsControl, rsAcessoB;
        try {
            sql_Acesso_Moradores   = "SELECT BLOCO,APTO FROM MORADORES WHERE BLOCO<>' ' and APTO<>'0000' AND Upper(CONDOMINO) NOT LIKE ('%VISITANTE%')"
                                    +" AND Upper(CONDOMINO) NOT LIKE ('%HOSPEDE%') GROUP BY BLOCO, APTO ORDER BY BLOCO,APTO";
            crud_Control_Unidades  = "INSERT INTO UNIDADES(id,quadra,lote,status,proprietario) values(?,?,?,?,?)";
            sql_Busca_Proprietario = "SELECT bloco,apto, condomino,proprietario from moradores where condomino=proprietario";
            rsAcessoB    = stmFirebird.executeQuery(sql_Busca_Proprietario);
            rsAcessoA    = stmFirebird.executeQuery(sql_Acesso_Moradores);
            while (rsAcessoA.next()) {    
                stInsert = conSQLite.prepareStatement(crud_Control_Unidades);
                id++;
                stInsert.setInt(1, id);
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
                stInsert.setInt(3,rsAcessoA.getInt("APTO"));
                stInsert.setInt(4,0);
                /**********************************************************************
                 *  busca a quadra no banco CONTROL GUARITA, caso nao exista na base
                 *  insere com valor 0
                 **********************************************************************/
                SQL       = "SELECT ID FROM QUADRAS WHERE DESCRICAO='"+rsAcessoA.getString("BLOCO")+"'";
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
               
                System.out.println(rsAcessoA.getInt("APTO")+"  em string: "+rsAcessoA.getString("APTO"));

                try{
                    stInsert.execute();
                    //stsq.close();
                } catch (SQLException | NumberFormatException ex) {
                    System.out.println(ex.toString());
                }                
            }

        } catch (SQLException | NumberFormatException ex) {
            System.out.println(ex.toString());
        }
    }
    public void tb_acionadores() throws SQLException {
        Statement stmt;
        PreparedStatement stsq;
        String sql, iSql, tipo, testeHexa = null;
        ResultSet rs;
        int Codigo, Morador, id = 0, cTipo = 0;
        long dataDeHoje=System.currentTimeMillis()/1000;  
        try {
            /**********************************************************************************************************
            *                       TRATAMENTO DA TABELA ACIONADORES
            ***********************************************************************************************************/
            sql  = "SELECT CODMORADOR,TIPOACIONADOR,NUMACIONADOR from CARTAOMORADORES ORDER BY DATACADASTRO" ;
            iSql = "INSERT INTO ACIONADORES(id,tipo,codigo,usenha,morador,dataCriacao,enviado) values(?,?,?,?,?,?,?)";
            stmt = conFirebird.createStatement();
            stsq = conSQLite.prepareStatement(iSql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                id = ++id;
                tipo = rs.getString("TIPOACIONADOR");
                /**********************************************************************************************************
                 *     verificar se os dados inseridos no tipo do acionador/dispositivo foi imputado como INT ou HEXA
                 *     efetuar teste de bancada
                 *********************************************************************************************************/
                testeHexa = rs.getString("NUMACIONADOR");
                if (eHexa(testeHexa))
                {
                    Codigo  = Integer.parseInt(rs.getString("NUMACIONADOR"),16);
                }else{
                    Codigo = rs.getInt("NUMACIONADOR");
                } 
                /**********************************
                //  FIM DA VERIFICACAO DO TIPO
                ***********************************/
                Morador = rs.getInt("CODMORADOR");
                System.out.println(Morador+" hexa: "+eHexa(testeHexa)+" tipo : " +tipo + "  Codigo: "+ Codigo + " Data: "+dataDeHoje);
                
                if(tipo=="CON"){
                    cTipo = 1;
                }else if(tipo=="TAG"){
                    cTipo = 2;
                }else if(tipo=="CAR"){
                    cTipo = 3;
                }else if(tipo=="TAP"){
                    cTipo = 6;
                }else{
                    cTipo = 0;
                }
                stsq.setInt(1, id);
                stsq.setInt(2, cTipo);
                stsq.setInt(3, Codigo);
                stsq.setInt(4, 0);
                stsq.setInt(5, Morador);
                stsq.setLong(6, dataDeHoje);;
                stsq.setInt(7, 0);
                try{
                    stsq.execute();
                    stsq.close();
                } catch (SQLException | NumberFormatException ex) {
                    System.out.println(ex.toString());
                }                
            }
        } catch (SQLException | NumberFormatException ex) {
            System.out.println(ex.toString());
        }
    }
    static boolean eHexa(String letras)    {    
        String Sequencia = "ABCDEF";
        for (int x=0; x < Sequencia.length(); x++){
            for (int i=0; i < letras.length(); i++){
                if(letras.charAt(i)==Sequencia.charAt(x)){
                    System.out.println("Letras: "+letras+" Sequencia:"+ Sequencia+ " i: "+x+" caracter:"+Sequencia.charAt(x));
                    return true;
                }
            }
        }
        return false;
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
    public int isMorador(String bloco, String apto) throws SQLException{
        String SQL =  "SELECT bloco,apto, condomino,proprietario from moradores where condomino=proprietario and bloco='"+bloco+"' "
                    +"and apto='"+apto+"'";
                
        ResultSet rsAcessoB   = stmFirebird.executeQuery(SQL);
        if(rsAcessoB.next()){
            return 1;
        }else{
            return 0;
        }
    }
}
