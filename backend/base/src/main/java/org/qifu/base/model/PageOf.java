/* 
 * Copyright 2019-2021 qifu of copyright Chen Xin Nien
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * -----------------------------------------------------------------------
 * 
 * author: 	Chen Xin Nien
 * contact: chen.xin.nien@gmail.com
 * 
 */
package org.qifu.base.model;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.qifu.base.Constants;

import io.swagger.v3.oas.annotations.Parameter;

public class PageOf implements java.io.Serializable {
	private static final long serialVersionUID = -3060749245195776228L;
	
	@Parameter(hidden = true)
	protected static final int[] Rows={10, 30, 50, 100}; // 要配合 ui.grid.htm.flt
	
	public static final int DEFAULT_ROW = Rows[0];
	
	private String countSize="0"; // count record 頁面grid資料count的筆數
	private String showRow=DEFAULT_ROW+""; // a page show size 頁面要顯示grid row比數
	private String size="1"; // page size 總共有幾頁
	private String select="1"; // select page of 目前所在的頁面 , 下拉選到的頁數
	private String orderBy = ""; // 欄位
	private String sortType = ""; // ASC , DESC
	
	public PageOf() {
		
	}
	
	public PageOf(String select, String size, String showRow, String countSize) {
		this.select=select;
		this.size=size;
		this.showRow=showRow;
		this.countSize=countSize;
	}

	public String getCountSize() {
		return countSize;
	}

	public void setCountSize(String countSize) {
		this.countSize = countSize;
	}

	public String getShowRow() {
		boolean c = false;
		int r = NumberUtils.toInt(this.showRow, 0);
		for (int i=0; !c && i<Rows.length; i++) { // 在 bambooCORE 中只允許這幾個Row數
			if (r == Rows[i]) {
				c = true;
			}
		}
		return ( c ? showRow : String.valueOf(DEFAULT_ROW) );
	}

	public void setShowRow(String showRow) {
		this.showRow = showRow;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getSelect() {
		if (NumberUtils.toInt(this.select, 0) < 1 || NumberUtils.toInt(this.select, 0) > 1000000) { // 在 bambooCORE 中不允許
			return "1";
		}
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
	}
	
	public String getOrderBy() {
		if (null == orderBy) {
			orderBy = "";
		}
		orderBy = StringUtils.replace(orderBy, " ", "");
		orderBy = StringUtils.replace(orderBy, "\r", "");
		orderBy = StringUtils.replace(orderBy, "\t", "");
		orderBy = StringUtils.replace(orderBy, "\n", "");
		orderBy = StringUtils.replace(orderBy, ";", "");
		orderBy = StringUtils.replace(orderBy, "'", "");
		orderBy = StringUtils.replace(orderBy, "-", "");
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
	public String getSortType() {
		if (!StringUtils.isBlank(sortType)) {
			sortType = sortType.toUpperCase().trim();
			if (!SortType.isAllow(sortType)) {
				sortType = SortType.ASC;
			}
		}
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public int getIntegerValue(final String str) {
		if (str==null )
			return 0;		
		return org.apache.commons.lang3.math.NumberUtils.toInt(str, 0);
	}
	
	public long getLongValue(final String str) {
		if (str==null )
			return 0;		
		return org.apache.commons.lang3.math.NumberUtils.toLong(str, 0L);
	}	
	
	@Parameter(hidden = true)
	public void toCalculateSize() {		
		int mySize=1;
		int myShowRow=this.getIntegerValue(this.getShowRow() );
		int myCountSize=this.getIntegerValue(this.getCountSize() );
		if (myCountSize>0 && myShowRow>0 ) {
			mySize=myCountSize/myShowRow;
			if (myCountSize%myShowRow>0 ) {
				mySize+=1;
			}
		}
		this.setSize(mySize+"");	
		if ( NumberUtils.toInt(this.select, 0) > mySize ) { // 2017-06-30 bug fix add
			this.select = mySize + "";
		}		
	}
	
	/**
	 * 2019-09-10
	 * 
	 * @param currentStartRow
	 */
	@Parameter(hidden = true)
	public void toCalculateSize(int currentStartRow) {
		int mySize=1;
		int myShowRow=this.getIntegerValue(this.getShowRow() );
		int myCountSize=this.getIntegerValue(this.getCountSize() );
		if (myCountSize>0 && myShowRow>0 ) {
			mySize=myCountSize/myShowRow;
			if (myCountSize%myShowRow>0 ) {
				mySize+=1;
			}
		}
		this.setSize(mySize+"");	
		if ( NumberUtils.toInt(this.select, 0) > mySize ) { // 2017-06-30 bug fix add
			if (currentStartRow == 0) {
				this.select = "1";
			} else {
				this.select = mySize + "";
			}
		}
	}
	
	/**
	 * 把要代入查grid的 hql 的 map 參數加上 order by 條件
	 * orderBy		如 kpi.id, kpi.name
	 * sortType		如 ASC 或 DESC
	 * @param queryParam
	 */
	@Parameter(hidden = true)
	public Map<String, Object> setQueryOrderSortParameter(Map<String, Object> queryParam) {
		if (queryParam == null) {
			return queryParam;
		}
		if (queryParam.get(Constants.RESERVED_PARAM_NAME_QUERY_ORDER_BY) == null &&  (!StringUtils.isBlank(this.getOrderBy()))) {
			queryParam.put(Constants.RESERVED_PARAM_NAME_QUERY_ORDER_BY, this.getOrderBy());
		}
		if (queryParam.get(Constants.RESERVED_PARAM_NAME_QUERY_SORT_TYPE) == null &&  (!StringUtils.isBlank(this.getSortType()))) {
			queryParam.put(Constants.RESERVED_PARAM_NAME_QUERY_SORT_TYPE, this.getSortType());	
		}
		return queryParam;
	}
	
	@Parameter(hidden = true)
	public PageOf orderBy(String orderBy) {
		this.setOrderBy(orderBy);
		return this;
	}
	
	public PageOf sortBy(String sortBy) {
		this.setSortType(sortBy);
		return this;
	}
	
	@Parameter(hidden = true)
	public PageOf sortTypeAsc() {
		this.sortBy( SortType.ASC );
		return this;
	}
	
	@Parameter(hidden = true)
	public PageOf sortTypeDesc() {
		this.sortBy( SortType.DESC );
		return this;
	}	
	
	// for postgresql, ex: SELECT * FROM "tb_prog" ORDER BY "PROG_ID", "NAME" ASC
	@Parameter(hidden = true)
	public PageOf andFieldWrap() {
		if (StringUtils.isBlank(this.orderBy)) {
			return this;
		}
		this.orderBy = StringUtils.deleteWhitespace(this.orderBy);
		this.orderBy = StringUtils.replace(this.orderBy, ",,", ",");
		if (this.orderBy.endsWith(",")) {
			this.orderBy = this.orderBy.substring(0, this.orderBy.length()-1);
		}
		if (this.orderBy.indexOf(",") == -1) {
			if (!(this.orderBy.startsWith("\"") && this.orderBy.endsWith("\""))) {
				this.orderBy = "\"" + this.orderBy + "\"";
			}
			return this;
		}
		String[] tmp = this.orderBy.split(",");
		StringBuilder newOrderBy = new StringBuilder();
		for (int i = 0; i < tmp.length; i++) {
			if (!(tmp[i].startsWith("\"") && tmp[i].endsWith("\""))) {
				tmp[i] = "\"" + tmp[i] + "\"";
			}
			newOrderBy.append(tmp[i]);
			if ((i+1) < tmp.length) {
				newOrderBy.append(",");
			}
		}
		this.orderBy = newOrderBy.toString();
		return this;
	}
	
}
