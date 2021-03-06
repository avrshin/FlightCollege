/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.process;


/**
 *	Document Options Interface
 *	
 *  @author Carlos Ruiz
 */
public interface DocOptions
{
	/**
	 * 	Customize Valid Actions
	 * @param docStatus
	 * @param processing
	 * @param orderType
	 * @param isSOTrx
	 * @param AD_Table_ID
	 * @param docAction
	 * @param options
	 * @param index
	 * @return Number of valid options
	 */
	public int customizeValidActions (String docStatus, Object processing, 
			String orderType, String isSOTrx, int AD_Table_ID, String[] docAction, String[] options, int index);
	
	/**************************************************************************
	 * 	Get Action Options based on current Status
	 *	@return array of actions
	 */
	public String[] getCustomizedActionOptions();

	/**
	 * Process a custom action similar to the {@link org.compiere.process.DocumentEngine#processIt(String) processIt()} method in DocumentEngine.java.
	 * @param customAction - the two letter action code.
	 * @return true is successful.
	 */
	public boolean processCustomAction(String customAction);


}