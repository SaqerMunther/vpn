/*
 * package com.app.dev.cmon.components;
 * 
 * import java.util.ArrayList; import java.util.List;
 * 
 * import com.app.dev.cmon.controllers.DataAccess; import
 * com.arabbank.dev.utility.Pair;
 * 
 * public class ResultTable { private List<PercentageCard> pCards = new
 * ArrayList<>(); private DataAccess da = new DataAccess(); public
 * ResultTable(int viewId,int subViewId,String country,String type) {
 * 
 * if(type.toLowerCase().contains("cov")) { Pair<Integer,Double> item1
 * =getFirstObject(da.getCoverageUpper(2,country,viewId,subViewId));
 * Pair<Integer,Double>item2
 * =getFirstObject(da.getCoverageUpper(1,country,viewId,subViewId));
 * Pair<Integer,Double> item3
 * =getFirstObject(da.getCoverageUpper(0,country,viewId,subViewId));
 * pCards.add(new PercentageCard(item1.getFirst(),item1.getSecond(),type,true));
 * pCards.add(new PercentageCard(item2.getFirst(),item2.getSecond(),type,true));
 * pCards.add(new PercentageCard(item3.getFirst(),item3.getSecond(),type,true));
 * 
 * pCards.add(new PercentageCard((int)da.getCoverageTotal(viewId,subViewId,3, 6,
 * 2, country).get(0).getFirst(),1,type,false)); pCards.add(new
 * PercentageCard((int)da.getCoverageTotal(viewId,subViewId,3,6 , 1,
 * country).get(0).getFirst(),1,type,false)); pCards.add(new
 * PercentageCard((int)da.getCoverageTotal(viewId,subViewId,3, 6, 0,
 * country).get(0).getFirst(),1,type,false));
 * 
 * 
 * pCards.add(new
 * PercentageCard((int)da.getCoverageTotal(viewId,subViewId,6,1000, 2,
 * country).get(0).getFirst(),1,type,false)); pCards.add(new
 * PercentageCard((int)da.getCoverageTotal(viewId,subViewId,6,1000, 1,
 * country).get(0).getFirst(),1,type,false)); pCards.add(new
 * PercentageCard((int)da.getCoverageTotal(viewId,subViewId,6,1000, 0,
 * country).get(0).getFirst(),1,type,false)); } else { Pair<Integer,Double>
 * item1 =getFirstObject(da.getCompUpper(2, viewId, subViewId, country, 0));
 * Pair<Integer,Double>item2 =getFirstObject(da.getCompUpper(1, viewId,
 * subViewId, country, 0)); Pair<Integer,Double> item3
 * =getFirstObject(da.getCompUpper(0, viewId, subViewId, country, 0));
 * Pair<Integer,Double> item4 =getFirstObject(da.getCompUpper(2, viewId,
 * subViewId, country, 1)); Pair<Integer,Double>item5
 * =getFirstObject(da.getCompUpper(1, viewId, subViewId, country, 1));
 * Pair<Integer,Double> item6 =getFirstObject(da.getCompUpper(0, viewId,
 * subViewId, country, 1)); pCards.add(new
 * PercentageCard(item1.getFirst(),item1.getSecond(),type,true)); pCards.add(new
 * PercentageCard(item2.getFirst(),item2.getSecond(),type,true)); pCards.add(new
 * PercentageCard(item3.getFirst(),item3.getSecond(),type,true)); pCards.add(new
 * PercentageCard(item4.getFirst(),item4.getSecond(),type,true)); pCards.add(new
 * PercentageCard(item5.getFirst(),item5.getSecond(),type,true)); pCards.add(new
 * PercentageCard(item6.getFirst(),item6.getSecond(),type,true)); }
 * 
 * }
 * 
 * private Pair<Integer,Double> getFirstObject(List<Pair<Integer,Double>> list){
 * if(list.size() == 0) return new Pair<Integer,Double>(0,1.0); if( list == null
 * ) return new Pair<Integer,Double>(0,1.0); return list.get(0); }
 * 
 * public List<PercentageCard> getpCards() { return pCards; } }
 */