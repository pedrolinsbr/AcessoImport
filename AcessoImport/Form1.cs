using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace AcessoImport
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            DataTable dt = new DataTable();

            String insSQL = "select * from acionadores";
            String strConn = @"Data Source=C:\dados\MacorattiSQLite.db";

          /*  SQLiteConnection conn = new SQLiteConnection(strConn);

            SQLiteDataAdapter da = new SQLiteDataAdapter(insSQL, strConn);
            da.Fill(dt);
            gdvA.DataSource = dt.DefaultView;*/
        }
    }
}
