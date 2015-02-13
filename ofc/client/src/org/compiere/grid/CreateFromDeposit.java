/******************************************************************************
 * Copyright (C) 2013 Michael McKay                                           *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/
package org.compiere.grid;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.minigrid.IMiniTable;
import org.compiere.model.GridTab;
import org.compiere.model.MBankAccount;
import org.compiere.model.MBankDeposit;
import org.compiere.model.MBankDepositLine;
import org.compiere.model.MPayment;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

/**
 *  Create Transactions for Bank Deposits
 *
 */
public class CreateFromDeposit extends CreateFrom 
{
	public MBankAccount bankAccount;
	
	/**
	 *  Protected Constructor
	 *  @param mTab MTab
	 */
	public CreateFromDeposit(GridTab mTab)
	{
		super(mTab);
		log.info(mTab.toString());
	}   //  VCreateFromInvoice

	/**
	 *  Dynamic Init
	 *  @return true if initialized
	 */
	public boolean dynInit() throws Exception
	{
		log.config("");
		setTitle(Msg.translate(Env.getCtx(), "C_BankDeposit_ID") + " .. " + Msg.translate(Env.getCtx(), "CreateFrom"));
		
		return true;
	}   //  dynInit
	
	/**************************************************************************
	 *	Construct SQL Where Clause and define parameters
	 *  (setParameters needs to set parameters)
	 *  Includes first AND
	 *  @return sql where clause
	 */
	public String getSQLWhere(String DocumentNo, Object BPartner, Object DateFrom, Object DateTo, 
			Object AmtFrom, Object AmtTo, Object DocType, Object TenderType, Object CreditCardType, String AuthCode)
	{
		StringBuffer sql = new StringBuffer("WHERE p.Processed='Y' AND p.IsReconciled='N'"
		+ " AND p.DocStatus IN ('CO','CL','RE','VO') AND p.PayAmt<>0" 
		+ "	AND NOT (p.TrxType = 'X' AND p.TenderType = 'G')" // Exclude deposits
		+ " AND p.C_BankAccount_ID = ?");
		
	    sql.append( " AND NOT EXISTS (SELECT * FROM C_BankDepositLine l " 
		//	not included in another bank deposit - possibly open
			+ "WHERE p.C_Payment_ID=l.C_Payment_ID AND l.TrxAmt > 0)");
		
		if (DocumentNo.length() > 0)
			sql.append(" AND UPPER(p.DocumentNo) LIKE ?");
		//
		if (BPartner != null)
			sql.append(" AND p.C_BPartner_ID=?");
		//
		if (DateFrom != null || DateTo != null)
		{
			Timestamp from = (Timestamp) DateFrom;
			Timestamp to = (Timestamp) DateTo;
			if (from == null && to != null)
				sql.append(" AND TRUNC(p.DateTrx, 'DD') <= ?");
			else if (from != null && to == null)
				sql.append(" AND TRUNC(p.DateTrx, 'DD') >= ?");
			else if (from != null && to != null)
				sql.append(" AND TRUNC(p.DateTrx, 'DD') BETWEEN ? AND ?");
		}
		//
		if (AmtFrom != null || AmtTo != null)
		{
			BigDecimal from = (BigDecimal) AmtFrom;
			BigDecimal to = (BigDecimal) AmtTo;
			if (from == null && to != null)
				sql.append(" AND p.PayAmt <= ?");
			else if (from != null && to == null)
				sql.append(" AND p.PayAmt >= ?");
			else if (from != null && to != null)
				sql.append(" AND p.PayAmt BETWEEN ? AND ?");
		}
		
		if(DocType!=null)
			sql.append(" AND p.C_DocType_ID=?");
		if(TenderType != null && TenderType.toString().length() > 0)
			sql.append(" AND p.TenderType=?");
		if(CreditCardType != null && CreditCardType.toString().length() > 0)
			sql.append(" AND p.CreditCardType=?");
		if(AuthCode.length() > 0 )
			sql.append(" AND p.R_AuthCode LIKE ?");

		log.fine(sql.toString());
		return sql.toString();
	}	//	getSQLWhere
	
	/**
	 *  Set Parameters for Query.
	 *  (as defined in getSQLWhere)
	 *  @param pstmt Deposit
	 *  @param forCount for counting records
	 *  @throws SQLException
	 */
	void setParameters(PreparedStatement pstmt, boolean forCount, 
			String DocumentNo, Object BPartner, Object DateFrom, Object DateTo, 
			Object AmtFrom, Object AmtTo, Object DocType, Object TenderType, Object CreditCardType, String AuthCode) 
	throws SQLException
	{
		int index = 1;
		
		pstmt.setInt(index++, bankAccount.getC_BankAccount_ID());
		
		if (DocumentNo.length() > 0)
			pstmt.setString(index++, getSQLText(DocumentNo));
		//
		if (BPartner != null)
		{
			Integer bp = (Integer) BPartner;
			pstmt.setInt(index++, bp.intValue());
			log.fine("BPartner=" + bp);
		}
		//
		if (DateFrom != null || DateTo != null)
		{
			Timestamp from = (Timestamp) DateFrom;
			Timestamp to = (Timestamp) DateTo;
			log.fine("Date From=" + from + ", To=" + to);
			if (from == null && to != null)
				pstmt.setTimestamp(index++, to);
			else if (from != null && to == null)
				pstmt.setTimestamp(index++, from);
			else if (from != null && to != null)
			{
				pstmt.setTimestamp(index++, from);
				pstmt.setTimestamp(index++, to);
			}
		}
		//
		if (AmtFrom != null || AmtTo != null)
		{
			BigDecimal from = (BigDecimal) AmtFrom;
			BigDecimal to = (BigDecimal) AmtTo;
			log.fine("Amt From=" + from + ", To=" + to);
			if (from == null && to != null)
				pstmt.setBigDecimal(index++, to);
			else if (from != null && to == null)
				pstmt.setBigDecimal(index++, from);
			else if (from != null && to != null)
			{
				pstmt.setBigDecimal(index++, from);
				pstmt.setBigDecimal(index++, to);
			}
		}
		if(DocType!=null)
			pstmt.setInt(index++, (Integer) DocType);
		if(TenderType!=null  && TenderType.toString().length() > 0 )
			pstmt.setString(index++, (String) TenderType);
		if(CreditCardType != null && CreditCardType.toString().length() > 0)
			pstmt.setString(index++, (String) CreditCardType);
		if(AuthCode.length() > 0 )
			pstmt.setString(index++, getSQLText(AuthCode));

	}   //  setParameters
	
	/**
	 *  Get SQL WHERE parameter
	 *  @param f field
	 *  @return Upper case text with % at the end
	 */
	private String getSQLText (String text)
	{
		String s = text.toUpperCase();
		if (!s.endsWith("%"))
			s += "%";
		log.fine( "String=" + s);
		return s;
	}   //  getSQLText
	
	protected Vector<Vector<Object>> getBankData(String DocumentNo, Object BPartner, Object DateFrom, Object DateTo, 
			Object AmtFrom, Object AmtTo, Object DocType, Object TenderType, Object CreditCardType, String AuthCode)
	{
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		
		String sql = "SELECT p.DateTrx,p.C_Payment_ID,p.DocumentNo, p.C_Currency_ID,c.ISO_Code, p.PayAmt,"
			+ "currencyConvert(p.PayAmt,p.C_Currency_ID,ba.C_Currency_ID,pay.DateAcct,p.C_ConversionType_ID,p.AD_Client_ID,p.AD_Org_ID),"
			+ " bp.Name "
			+ "FROM C_BankAccount ba"
			+ " INNER JOIN C_Payment_v p ON (p.C_BankAccount_ID=ba.C_BankAccount_ID)"
			+ " INNER JOIN C_Payment pay ON (p.C_Payment_ID=pay.C_Payment_ID)"
			+ " INNER JOIN C_Currency c ON (p.C_Currency_ID=c.C_Currency_ID)"
			+ " LEFT OUTER JOIN C_BPartner bp ON (p.C_BPartner_ID=bp.C_BPartner_ID) ";

		sql = sql + getSQLWhere(DocumentNo, BPartner, DateFrom, DateTo, AmtFrom, AmtTo, DocType, TenderType, CreditCardType, AuthCode) + " ORDER BY p.DateTrx";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), null);
			setParameters(pstmt, false, DocumentNo, BPartner, DateFrom, DateTo, AmtFrom, AmtTo, DocType, TenderType, CreditCardType, AuthCode);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>(6);
				line.add(new Boolean(false));       //  0-Selection
				line.add(rs.getTimestamp(1));       //  1-DateTrx
				KeyNamePair pp = new KeyNamePair(rs.getInt(2), rs.getString(3));
				line.add(pp);                       //  2-C_Payment_ID
				pp = new KeyNamePair(rs.getInt(4), rs.getString(5));
				line.add(pp);                       //  3-Currency
				line.add(rs.getBigDecimal(6));      //  4-PayAmt
				line.add(rs.getBigDecimal(7));      //  5-Conv Amt
				line.add(rs.getString(8));      	//  6-BParner
				data.add(line);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		
		return data;
	}
	
	public void info()
	{
		
	}
	
	protected void configureMiniTable (IMiniTable miniTable)
	{
		miniTable.setColumnClass(0, Boolean.class, false);      //  0-Selection
		miniTable.setColumnClass(1, Timestamp.class, true);     //  1-TrxDate
		miniTable.setColumnClass(2, String.class, true);        //  2-Payment
		miniTable.setColumnClass(3, String.class, true);        //  3-Currency
		miniTable.setColumnClass(4, BigDecimal.class, true);    //  4-Amount
		miniTable.setColumnClass(5, BigDecimal.class, true);    //  5-ConvAmount
		miniTable.setColumnClass(6, String.class, true);    	//  6-BPartner
		//  Table UI
		miniTable.autoSize();
	}

	/**
	 *  Save Deposit - Insert Data
	 *  @return true if saved
	 */
	public boolean save(IMiniTable miniTable, String trxName)
	{
		//  fixed values
		int C_BankDeposit_ID = ((Integer)getGridTab().getValue("C_BankDeposit_ID")).intValue();
		MBankDeposit bd = new MBankDeposit (Env.getCtx(), C_BankDeposit_ID, trxName);
		log.config(bd.toString());

		//  Lines
		for (int i = 0; i < miniTable.getRowCount(); i++)
		{
			if (((Boolean)miniTable.getValueAt(i, 0)).booleanValue())
			{
				Timestamp trxDate = (Timestamp)miniTable.getValueAt(i, 1);  //  1-DateTrx
				KeyNamePair pp = (KeyNamePair)miniTable.getValueAt(i, 2);   //  2-C_Payment_ID
				int C_Payment_ID = pp.getKey();
				MPayment pmt = new MPayment(Env.getCtx(), C_Payment_ID, trxName);
				pp = (KeyNamePair)miniTable.getValueAt(i, 3);               //  3-Currency
				int C_Currency_ID = pp.getKey();
				BigDecimal PayAmt = (BigDecimal)miniTable.getValueAt(i, 4); //  4-Payment amount in payment currency
				BigDecimal ConvAmt = (BigDecimal)miniTable.getValueAt(i, 5); //  5- Conv Amt in bank account currency

				log.fine("Line Date=" + trxDate
					+ ", Payment=" + C_Payment_ID + ", Currency=" + C_Currency_ID + ", Amt=" + PayAmt + ", Converted Amt=" + ConvAmt);
				//	
				MBankDepositLine bdl = new MBankDepositLine (bd);
				bdl.setPayment(pmt);
				bdl.setValutaDate(bd.getDepositDate());
				
				if (!bdl.save())
					log.log(Level.SEVERE, "Line not created #" + i);
			}   //   if selected
		}   //  for all rows
		return true;
	}   //  save
	
	protected Vector<String> getOISColumnNames()
	{
		//  Header Info
		Vector<String> columnNames = new Vector<String>(6);
		columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
		columnNames.add(Msg.translate(Env.getCtx(), "Date"));
		columnNames.add(Msg.getElement(Env.getCtx(), "C_Payment_ID"));
		columnNames.add(Msg.translate(Env.getCtx(), "C_Currency_ID"));
		columnNames.add(Msg.translate(Env.getCtx(), "Amount"));
		columnNames.add(Msg.translate(Env.getCtx(), "ConvertedAmount"));
		columnNames.add(Msg.translate(Env.getCtx(), "C_BPartner_ID"));
	    
	    return columnNames;
	}
}
