package com.madx.cherry.common.util;

import java.io.File;
import java.io.FileWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by A-mdx on 2017/6/19.
 */
public class GenPO {

    private String driver = ""; //驱动
    private String url = ""; //数据库访问串
    private String userName = ""; //数据库用户名
    private String password = ""; //数据库密码
    private String tableName = ""; //要生成jopo对象的表名,使用;进行分割
    private String tableMatchPattern = ""; //数据库表名匹配模式
    private String matchPattern = ""; //是否启用数据库表名匹配模式功能,启用后tableName属性不被使用
    private String package_name="package uap.web.chanjmgr.po;";



    public static void main(String[] args) {
        // TODO Auto-generated method stub
        //GenPO d2j = new GenPO(true);
        GenPO d2j = new GenPO(
                // driver
                "com.mysql.jdbc.Driver",
                //url
                "jdbc:mysql://localhost:3306/cherry?useUnicode=true&characterEncoding=utf-8" ,
                //userName
                "root" ,
                //password
                "9295" ,
                //tableName,
                "sys_user",
                // + ";tm_account;tt_account_change;ti_chanj_callback_status;tm_account_type;ti_chanj_get_status",
                //tableMatchPattern
                "false",
                // matchPattern
                ""
        );
        System.out.println(d2j.getDriver());
        System.out.println(d2j.getUrl());
        System.out.println(d2j.getUserName());
        System.out.println(d2j.getPassword());
        System.out.println(d2j.getTableName());
        System.out.println(d2j.getTableMatchPattern());
        System.out.println(d2j.getMatchPattern());
        d2j.init(0);
        //d2j.init(StaticVar.COMMONLYTYPE);
/*     GenPO  d2j2 = new GenPO(
 			// driver
 			 "com.mysql.jdbc.Driver",
 			//url
 			 "jdbc:mysql://127.0.0.1:3306/d_saas?useUnicode=true&characterEncoding=utf-8" ,
 			 //userName
 			 "root" ,
 			 //password
 			 "1qazxsw2" ,
 			 //tableName,
 			 "tm_fix_code",
 			 //tableMatchPattern
 			 "false",
 			 // matchPattern
 			 ""
 			);
     String sql="select code from tm_fix_code";
     d2j2.initVO(StaticVar.OBJECTTYPE,sql);*/

        System.out.println("OK");
    }



    public GenPO(){}

    public GenPO(boolean init){
        if(init){
            ResourceBundle rb = ResourceBundle.getBundle("bqw.tool.DB2JavaBean");
            this.driver = rb.getString("driver");
            this.url = rb.getString("url");
            this.userName = rb.getString("userName");
            this.password = rb.getString("password");
            this.tableName = rb.getString("tableName");
            this.matchPattern = rb.getString("matchPattern");
            this.tableMatchPattern = rb.getString("tableMatchPattern");
        }

    }

    public GenPO(String baseName){
        ResourceBundle rb = ResourceBundle.getBundle(baseName);
        this.driver = rb.getString("driver");
        this.url = rb.getString("url");
        this.userName = rb.getString("userName");
        this.password = rb.getString("password");
        this.tableName = rb.getString("tableName");
        this.tableMatchPattern = rb.getString("tableMatchPattern");
        this.matchPattern = rb.getString("matchPattern");

    }

    public GenPO(String driver,String url,String userName,String password,String tableName,String tableMatchPattern,String matchPattern){
        this.driver = driver;
        this.password = password;
        this.userName = userName;
        this.url = url;
        this.tableName = tableName;
        this.tableMatchPattern = tableMatchPattern;
        this.matchPattern = matchPattern;
    }

    public void setDriver(String driver){
        this.driver = driver;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setTableName(String tableName){
        this.tableName = tableName;
    }

    public void setTableMatchPattern(String tableMatchPattern){
        this.tableMatchPattern=tableMatchPattern;
    }

    public void setMatchPattern(String matchPattern){
        this.matchPattern = matchPattern;
    }

    public String getDriver(){
        return this.driver;
    }

    public String getUrl(){
        return this.url;
    }

    public String getUserName(){
        return this.userName;
    }

    public String getPassword(){
        return this.password;
    }

    public String getTableName(){
        return this.tableName;
    }

    public String getTableMatchPattern(){
        return this.tableMatchPattern;
    }

    public String getMatchPattern(){
        return this.matchPattern;
    }
    public void init(int ObjectTypeOrCommonlyType){
        try{
            Class.forName(this.driver).newInstance();
            Connection conn = DriverManager.getConnection(this.url,this.userName, this.password);
            String [] tables = new String[0];
            ArrayList tableal = new ArrayList(20);
            if("true".equals(this.matchPattern)){
                DatabaseMetaData dbmd = conn.getMetaData();
                ResultSet dbmdrs = dbmd.getTables(null, this.userName.toUpperCase(), this.tableMatchPattern, new String[]{"TABLE"});
                while(dbmdrs.next()){
                    tableal.add(dbmdrs.getString(3));
                }
                dbmdrs.close();
                if(tableal.size()==0){
                    dbmdrs = dbmd.getTables(null, this.userName.toLowerCase(), this.tableMatchPattern, new String[]{"TABLE"});
                    while(dbmdrs.next()){
                        tableal.add(dbmdrs.getString(3));
                    }
                    dbmdrs.close();
                }
                if(tableal.size()==0){
                    dbmdrs = dbmd.getTables(null, this.userName, this.tableMatchPattern, new String[]{"TABLE"});
                    while(dbmdrs.next()){
                        tableal.add(dbmdrs.getString(3));
                    }
                    dbmdrs.close();
                    // }    
                    tables = new String[tableal.size()];
                    for(int ti = 0;ti<tableal.size();ti++)
                        tables[ti] = (String) tableal.get(ti);
                }
            }
            else{
                tables = this.tableName.split(";");
            }
            String strType;
            String strName;
            String className;
            String [] nameSect;
            StringBuilder tbn = new StringBuilder();
            StringBuilder tstr1 = new StringBuilder();
            StringBuilder tstr2 = new StringBuilder();
            File file = new File("JavaBean");
            if(!file.exists())file.mkdir();
            if(!file.isDirectory())file.mkdir();
            for(int i=0;i<tables.length;i++){
                String tableName=tables[i];
                nameSect = tableName.split("_");
                for(String ns : nameSect){
                    tbn.append(ns.substring(0,1).toUpperCase()+ns.substring(1).toLowerCase());
                }
                className = tbn.toString();
                tbn.delete(0,tbn.length());
/*       tstr1.append("import java.sql.*; ");
       tstr1.append("\n");
       tstr1.append("import javax.sql.*; ");
       tstr1.append("\n");*/

                tstr1.append(package_name);
                tstr1.append("\n");
                tstr1.append("import java.util.*; ");
                tstr1.append("\n");
                tstr1.append("import java.io.*; ");
                tstr1.append("\n");
                tstr1.append("import javax.persistence.Column;");
                tstr1.append("\n");
                tstr1.append("import javax.persistence.Entity;");
                tstr1.append("\n");
                tstr1.append("import javax.persistence.GeneratedValue;");
                tstr1.append("\n");
                tstr1.append("import javax.persistence.Id;");
                tstr1.append("\n");
                tstr1.append("import javax.persistence.Table;");
                tstr1.append("\n");
                tstr1.append("\n");
                tstr1.append("\n");
                tstr1.append("\n");
                tstr1.append("\n");
                tstr1.append("@Entity");
                tstr1.append("\n");
                tstr1.append("@Table(name=\""+tables[i]+"\")");
                tstr1.append("\n");

                className=className+"PO";
                tstr1.append("public class "+className+" implements Serializable{ ");
                tstr1.append("\n");
                tstr1.append("\n");

                tstr1.append("private static final long serialVersionUID = 1L;");
                tstr1.append("\n");

                try{
                    System.out.println(tables[i]);
                    Statement statement = conn.createStatement();
                    ResultSet rs = statement.executeQuery("select * from "+tables[i]);
                    ResultSetMetaData rsd = rs.getMetaData();
                    int cc = rsd.getColumnCount();
                    for(int j=1;j<=cc;j++){
                        if(ObjectTypeOrCommonlyType == 0){
                            strType = this.getObjectType(rsd.getColumnType(j));
                        }
                        else{
                            strType = this.getCommonlyType(j);
                        }
                        if(strType == null)continue;

                        strName = rsd.getColumnName(j);
                        if (rsd.isAutoIncrement(j)){
                            tstr1.append("	@Id");
                            tstr1.append("\n");
                            tstr1.append("	@GeneratedValue");
                            tstr1.append("\n");
                            tstr1.append("	@Column(name=\""+strName.toLowerCase()+"\")");
                            tstr1.append("\n");
                            strName="id";
                        }else{
                            tstr1.append("	@Column(name=\""+strName.toLowerCase()+"\")");
                            tstr1.append("\n");
                        }


                        nameSect = strName.split("_");
                        for(String ns : nameSect){
                            tbn.append(ns.substring(0,1).toUpperCase()+ns.substring(1).toLowerCase());
                        }
                        strName = tbn.toString();
                        tbn.delete(0,tbn.length());

                        tstr1.append("	private "+strType+" "+strName.substring(0,1).toLowerCase()+strName.substring(1)+";");
                        tstr1.append("\n");
                        tstr1.append("\n");
                        tstr2.append("	public void set"+strName +"("+strType+" "+strName.substring(0,1).toLowerCase()+strName.substring(1)+"){");
                        tstr2.append("\n");
                        tstr2.append("    	this."+strName.substring(0,1).toLowerCase()+strName.substring(1)+" = "+strName.substring(0,1).toLowerCase()+strName.substring(1)+";");
                        tstr2.append("\n");
                        tstr2.append(" 	}");
                        tstr2.append("\n");
                        tstr2.append("\n");
                        tstr2.append("	public "+strType+" get"+strName+"(){");
                        tstr2.append("\n");
                        tstr2.append("    	return this."+strName.substring(0,1).toLowerCase()+strName.substring(1)+";");
                        tstr2.append("\n");
                        tstr2.append(" 	}");
                        tstr2.append("\n");
                        tstr2.append("\n");

                    }
                    rs.close();
                    statement.close();

                }
                catch(Exception tableE)
                {
                    tableE.printStackTrace();
                }
                tstr2.append("} ");
                tstr2.append("\n");
                tstr1.append(tstr2.toString());
                tstr1.append("\n");
                file = new File("JavaBean/"+className+".java");
                FileWriter fw = new FileWriter(file);
                fw.write(tstr1.toString());
                fw.flush();
                fw.close();
                tstr1.delete(0, tstr1.length());
                tstr2.delete(0, tstr2.length());
            }
            conn.close();
        }
        catch(Exception driverE){
            driverE.printStackTrace();
        }

    }

    public String getObjectType(int type){
        switch(type){
            case Types.ARRAY:return null;
            case Types.BIGINT:return "Long";
            case Types.BINARY:return null;
            case Types.BIT:return "Byte";
            case Types.BLOB:return "Blob";
            case Types.BOOLEAN:return "Boolean";
            case Types.CHAR:return "String";
            case Types.CLOB:return "Clob";
            case Types.DATALINK:return null;
            case Types.DATE:return "Date";
            case Types.DECIMAL:return "Double";
            case Types.DISTINCT:return null;
            case Types.DOUBLE:return "Double";
            case Types.FLOAT:return "Float";
            case Types.INTEGER:return "Integer";
            case Types.NUMERIC:return "Integer";
            case Types.JAVA_OBJECT:return null;
            case Types.LONGVARBINARY:return null;
            case Types.LONGVARCHAR:return null;
            case Types.NULL:return null;
            case Types.OTHER:return null;
            case Types.REAL:return null;
            case Types.REF:return null;
            case Types.SMALLINT:return "Short";
            case Types.STRUCT:return null;
            case Types.TIME:return "Time";
            //case Types.TIMESTAMP:return "Timestamp";
            case Types.TIMESTAMP:return "Date";
            case Types.TINYINT:return "Short";
            case Types.VARBINARY:return null;
            case Types.VARCHAR:return "String";
            default :return null;
        }
    }

    public String getCommonlyType(int type){
        switch(type){
            case Types.ARRAY:return null;
            case Types.BIGINT:return "long";
            case Types.BINARY:return null;
            case Types.BIT:return "byte";
            case Types.BLOB:return "String";
            case Types.BOOLEAN:return "boolean";
            case Types.CHAR:return "String";
            case Types.CLOB:return "String";
            case Types.DATALINK:return null;
            case Types.DATE:return "Date";
            case Types.DECIMAL:return "double";
            case Types.DISTINCT:return null;
            case Types.DOUBLE:return "double";
            case Types.FLOAT:return "float";
            case Types.INTEGER:return "int";
            case Types.NUMERIC:return "int";
            case Types.JAVA_OBJECT:return null;
            case Types.LONGVARBINARY:return null;
            case Types.LONGVARCHAR:return null;
            case Types.NULL:return null;
            case Types.OTHER:return null;
            case Types.REAL:return null;
            case Types.REF:return null;
            case Types.SMALLINT:return "short";
            case Types.STRUCT:return null;
            case Types.TIME:return "Time";
            case Types.TIMESTAMP:return "Timestamp";
            case Types.TINYINT:return "short";
            case Types.VARBINARY:return null;
            case Types.VARCHAR:return "String";
            default :return null;
        }
    }

    public void initVO(int ObjectTypeOrCommonlyType,String sql){
        try{
            Class.forName(this.driver).newInstance();
            Connection conn = DriverManager.getConnection(this.url,this.userName, this.password);
            String [] tables = new String[0];
            ArrayList tableal = new ArrayList(20);
            if("true".equals(this.matchPattern)){
                DatabaseMetaData dbmd = conn.getMetaData();
                ResultSet dbmdrs = dbmd.getTables(null, this.userName.toUpperCase(), this.tableMatchPattern, new String[]{"TABLE"});
                while(dbmdrs.next()){
                    tableal.add(dbmdrs.getString(3));
                }
                dbmdrs.close();
                if(tableal.size()==0){
                    dbmdrs = dbmd.getTables(null, this.userName.toLowerCase(), this.tableMatchPattern, new String[]{"TABLE"});
                    while(dbmdrs.next()){
                        tableal.add(dbmdrs.getString(3));
                    }
                    dbmdrs.close();
                }
                if(tableal.size()==0){
                    dbmdrs = dbmd.getTables(null, this.userName, this.tableMatchPattern, new String[]{"TABLE"});
                    while(dbmdrs.next()){
                        tableal.add(dbmdrs.getString(3));
                    }
                    dbmdrs.close();
                    // }    
                    tables = new String[tableal.size()];
                    for(int ti = 0;ti<tableal.size();ti++)
                        tables[ti] = (String) tableal.get(ti);
                }
            }
            else{
                tables = this.tableName.split(";");
            }
            String strType;
            String strName;
            String className;
            String [] nameSect;
            StringBuilder tbn = new StringBuilder();
            StringBuilder tstr1 = new StringBuilder();
            StringBuilder tstr2 = new StringBuilder();
            File file = new File("JavaBean");
            if(!file.exists())file.mkdir();
            if(!file.isDirectory())file.mkdir();
            for(int i=0;i<tables.length;i++){
                String tableName=tables[i];
                nameSect = tableName.split("_");
                for(String ns : nameSect){
                    tbn.append(ns.substring(0,1).toUpperCase()+ns.substring(1).toLowerCase());
                }
                className = tbn.toString();
                tbn.delete(0,tbn.length());

                tstr1.append(package_name);
                tstr1.append("\n");
                tstr1.append("import java.util.*; ");
                tstr1.append("\n");
                tstr1.append("import java.io.*; ");
                tstr1.append("\n");
                tstr1.append("import javax.persistence.Column;");
                tstr1.append("\n");
                tstr1.append("import javax.persistence.Entity;");
                tstr1.append("\n");
                tstr1.append("import javax.persistence.GeneratedValue;");
                tstr1.append("\n");
                tstr1.append("import javax.persistence.Id;");
                tstr1.append("\n");
                tstr1.append("import javax.persistence.Table;");
                tstr1.append("\n");
                tstr1.append("\n");
                tstr1.append("\n");
                tstr1.append("\n");
                tstr1.append("\n");

                className=className+"VO";
                tstr1.append("public class "+className+" implements Serializable{ ");
                tstr1.append("\n");
                tstr1.append("\n");

                tstr1.append("private static final long serialVersionUID = 1L;");
                tstr1.append("\n");

                try{
                    System.out.println(tables[i]);
                    Statement statement = conn.createStatement();
                    ResultSet rs = statement.executeQuery(sql);
                    ResultSetMetaData rsd = rs.getMetaData();
                    int cc = rsd.getColumnCount();
                    for(int j=1;j<=cc;j++){
                        if(ObjectTypeOrCommonlyType == 0){
                            strType = this.getObjectType(rsd.getColumnType(j));
                        }
                        else{
                            strType = this.getCommonlyType(j);
                        }
                        if(strType == null)continue;



                        strName = rsd.getColumnName(j);

                        nameSect = strName.split("_");
                        for(String ns : nameSect){
                            tbn.append(ns.substring(0,1).toUpperCase()+ns.substring(1).toLowerCase());
                        }
                        strName = tbn.toString();
                        tbn.delete(0,tbn.length());

                        tstr1.append("	private "+strType+" "+strName.substring(0,1).toLowerCase()+strName.substring(1)+";");
                        tstr1.append("\n");
                        tstr1.append("\n");
                        tstr2.append("	public void set"+strName +"("+strType+" "+strName.substring(0,1).toLowerCase()+strName.substring(1)+"){");
                        tstr2.append("\n");
                        tstr2.append("    	this."+strName.substring(0,1).toLowerCase()+strName.substring(1)+" = "+strName.substring(0,1).toLowerCase()+strName.substring(1)+";");
                        tstr2.append("\n");
                        tstr2.append(" 	}");
                        tstr2.append("\n");
                        tstr2.append("\n");
                        tstr2.append("	public "+strType+" get"+strName+"(){");
                        tstr2.append("\n");
                        tstr2.append("    	return this."+strName.substring(0,1).toLowerCase()+strName.substring(1)+";");
                        tstr2.append("\n");
                        tstr2.append(" 	}");
                        tstr2.append("\n");
                        tstr2.append("\n");

                    }
                    rs.close();
                    statement.close();

                }
                catch(Exception tableE)
                {
                    tableE.printStackTrace();
                }
                tstr2.append("} ");
                tstr2.append("\n");
                tstr1.append(tstr2.toString());
                tstr1.append("\n");
                file = new File("JavaBean/"+className+".java");
                FileWriter fw = new FileWriter(file);
                fw.write(tstr1.toString());
                fw.flush();
                fw.close();
                tstr1.delete(0, tstr1.length());
                tstr2.delete(0, tstr2.length());
            }
            conn.close();
        }
        catch(Exception driverE){
            driverE.printStackTrace();
        }

    }

}

