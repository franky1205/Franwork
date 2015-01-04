package org.franwork.core.util.group;

import java.math.BigDecimal;

/**
 * 偉康 Frankie 專用 Grouping 介面勿動 (再說一次 勿動)
 * 
 * 有任何問題需要修改請直接與我聯絡
 * 
 * E-MAIL : franky.chao@gmail.com
 * Mobile : 0972-500-080
 * 
 * @author WebComm Frankie
 *
 */
public interface GroupingElement {
	
	public boolean isContains(BigDecimal compareNo);
	
	public void setGroupingNoRange(BigDecimal groupingSeqNoBeg, BigDecimal groupingSeqNoEnd);
	
	public void setGroupingSeqNoBeg(BigDecimal groupingSeqNoBeg);
	
	public BigDecimal getGroupingSeqNoBeg();
	
	public void setGroupingSeqNoEnd(BigDecimal groupingSeqNoEnd);
	
	public BigDecimal getGroupingSeqNoEnd();
}
