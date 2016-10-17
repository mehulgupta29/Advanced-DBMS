package edu.stevens.cs562.queryprocessor;
import edu.stevens.cs562.queryprocessor.MfStructure;
import edu.stevens.cs562.queryprocessor.DatabaseOperations;
import java.sql.ResultSet;
import java.sql.SQLException;
public class QP {
private MfStructure[] mftable = new MfStructure[500];
private int counter;
 public void algorithm(){
 DatabaseOperations dbop = new DatabaseOperations();
 String query = "select * from sales";
 String newquery="";
 newquery = query +" where year=2008";
 if(dbop != null) {
 ResultSet rs = dbop.executeSaleQuery(newquery);
try {
  while( rs.next()) {
   int index = 0; 
   boolean isexist = false;
 String prod = rs.getString("prod");
while (index < counter) { 
if (mftable[index].prod.equals(prod)){
 isexist = true ;
break;
}
index++;
}
if (!isexist) {
 mftable[counter] = new MfStructure();
mftable[counter].prod = prod;
counter++;
}
}
rs.beforeFirst();
  while( rs.next()) {
   int index = 0; 
 String prod = rs.getString("prod");
int quant = rs.getInt("quant");
while (index < counter) { 
if (mftable[index].prod.equals(prod) && rs.getInt("month") == 1){
mftable[index].sum_quant_1 = mftable[index].sum_quant_1 + quant;
}
index++;
}
}
rs.beforeFirst();
  while( rs.next()) {
   int index = 0; 
 String prod = rs.getString("prod");
int quant = rs.getInt("quant");
while (index < counter) { 
if ( mftable[index].prod.equals(prod) && rs.getInt("month") == 2){
mftable[index].sum_quant_2 = mftable[index].sum_quant_2 + quant;
}
index++;
}
}
rs.beforeFirst();
  while( rs.next()) {
   int index = 0; 
 String prod = rs.getString("prod");
int quant = rs.getInt("quant");
while (index < counter) { 
if ( mftable[index].prod.equals(prod) && rs.getInt("month") == 3){
mftable[index].sum_quant_3 = mftable[index].sum_quant_3 + quant;
}
index++;
}
}
}
catch (SQLException e) {
 e.printStackTrace();
}
}
}
 public void displayResult(){ 
System.out.println("prod	|	mftable[index].sum_quant_1	|	mftable[index].sum_quant_2	|	mftable[index].sum_quant_3");
System.out.println("----------------------------------------------------------------");
for (int index = 0; index < counter; index++) {
System.out.println(mftable[index].prod+"	|	"+mftable[index].sum_quant_1+"	|	"+mftable[index].sum_quant_2+"	|	"+mftable[index].sum_quant_3);
}
}
 public static void main(String[] args){
  System.out.println("QP class main()");
QP qp = new QP();
qp.algorithm();
qp.displayResult();
 }
}
