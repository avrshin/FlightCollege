/******************************************************************************
 * Product: ADempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2006-2016 ADempiere Foundation, All Rights Reserved.         *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * or via info@adempiere.net or http://www.adempiere.net/license.html         *
 *****************************************************************************/
package com.mckayerp.ftu.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for FTU_Course_Grade
 *  @author Adempiere (generated) 
 *  @version Release 3.8.0
 */
public interface I_FTU_Course_Grade 
{

    /** TableName=FTU_Course_Grade */
    public static final String Table_Name = "FTU_Course_Grade";

    /** AD_Table_ID=1000013 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name CourseGrade */
    public static final String COLUMNNAME_CourseGrade = "CourseGrade";

	/** Set Grade.
	  * The grade or mark for that course.
	  */
	public void setCourseGrade (String CourseGrade);

	/** Get Grade.
	  * The grade or mark for that course.
	  */
	public String getCourseGrade();

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name FTU_Course_Grade_ID */
    public static final String COLUMNNAME_FTU_Course_Grade_ID = "FTU_Course_Grade_ID";

	/** Set Course Grade ID	  */
	public void setFTU_Course_Grade_ID (int FTU_Course_Grade_ID);

	/** Get Course Grade ID	  */
	public int getFTU_Course_Grade_ID();

    /** Column name FTU_Registered_Courses_ID */
    public static final String COLUMNNAME_FTU_Registered_Courses_ID = "FTU_Registered_Courses_ID";

	/** Set Registered Courses ID	  */
	public void setFTU_Registered_Courses_ID (int FTU_Registered_Courses_ID);

	/** Get Registered Courses ID	  */
	public int getFTU_Registered_Courses_ID();

	public com.mckayerp.ftu.model.I_FTU_Registered_Courses getFTU_Registered_Courses() throws RuntimeException;

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name IsGradeSubmitted */
    public static final String COLUMNNAME_IsGradeSubmitted = "IsGradeSubmitted";

	/** Set Grade Submitted	  */
	public void setIsGradeSubmitted (boolean IsGradeSubmitted);

	/** Get Grade Submitted	  */
	public boolean isGradeSubmitted();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();
}
