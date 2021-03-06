package com.mckayerp.ftu.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.model.Query;

public class MFTUInstructor extends X_FTU_Instructor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9070700766368831539L;

	public MFTUInstructor(Properties ctx, int FTU_Instructor_ID, String trxName) {
		super(ctx, FTU_Instructor_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public MFTUInstructor(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	public static MFTUInstructor get(Properties ctx, int FTU_Instructor_ID, String trxName) {
			if (FTU_Instructor_ID == 0)
				return null;
			return new MFTUInstructor(ctx, FTU_Instructor_ID, trxName);
	}

	public static MFTUInstructor getByBPartnerID(Properties ctx, int C_BPartner_ID) {
		
		if (C_BPartner_ID <= 0)
			return null;
		
		String where = MFTUInstructor.COLUMNNAME_C_BPartner_ID + "=" + C_BPartner_ID;
		
		return (MFTUInstructor) new Query(ctx, MFTUInstructor.Table_Name, where, null)
				.setClient_ID()
				.firstOnly();
	}

}
