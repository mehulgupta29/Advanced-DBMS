package edu.stevens.cs562.queryprocessor;

import edu.stevens.cs562.queryprocessor.MfStructure;
import edu.stevens.cs562.queryprocessor.DatabaseOperations;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QP {
	private MfStructure[] mftable = new MfStructure[500];
	private int counter;

	public void algorithm() {
		DatabaseOperations dbop = new DatabaseOperations();
		String query = "select * from sales";
		String newquery = "";
		newquery = query;
		if (dbop != null) {
			ResultSet rs = dbop.executeSaleQuery(newquery);
			try {
				while (rs.next()) {
					int index = 0;
					boolean isexist = false;
					String cust = rs.getString("cust");
					String prod = rs.getString("prod");
					while (index < counter) {
						if (mftable[index].cust.equals(cust) && mftable[index].prod.equals(prod)) {
							isexist = true;
							break;
						}
						index++;
					}
					if (!isexist) {
						mftable[counter] = new MfStructure();
						mftable[counter].cust = cust;
						mftable[counter].prod = prod;
						counter++;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void displayResult() {
		System.out.println("cust	|	prod");
		System.out.println("----------------------------------------------------------------");
		for (int index = 0; index < counter; index++) {
			System.out.println(mftable[index].cust + "	|	" + mftable[index].prod);
		}
	}

	public static void main(String[] args) {
		System.out.println("QP class main()");
		QP qp = new QP();
		qp.algorithm();
		qp.displayResult();
	}
}
