package com.free.app.ticket.util;

import java.util.Comparator;

import org.apache.commons.lang3.StringUtils;

import com.free.app.ticket.model.TrainInfo;

public class ComparatorTrain implements Comparator<TrainInfo> {
    
    /**
     * <比较两个车次购买优先级>
     * @param t1
     * @param t2
     * @return 返回1: t1>t2; -1: t1<t2;
     */
    @Override
    public int compare(TrainInfo t1, TrainInfo t2) {
        TrainType type1 = getTrainType(t1.getStation_train_code());
        TrainType type2 = getTrainType(t2.getStation_train_code());
        
        int compareValue = type1.value - type2.value;
        if (compareValue == 0) {//先比较类型
            return -type1.compare(t1, t2);//按席别比较
        }
        else {
            return -compareValue;
        }
    }
    
    enum TrainType {
        GAOTIE("高铁/城际/动车", 9, CompareType.GAOTIE), ZHIDA("直达", 5, CompareType.OTHER), TEKUAI("特快", 4, CompareType.OTHER), KUAISU(
            "快速", 3, CompareType.OTHER), LINKE("临客", -1, CompareType.OTHER), QITA("临客", -2, CompareType.OTHER);
        public int getValue() {
            return value;
        }
        
        private String label;
        
        private int value;
        
        private CompareType type;
        
        TrainType(String label, int value, CompareType type) {
            this.label = label;
            this.value = value;
            this.type = type;
        }
        
        public int compare(TrainInfo t1, TrainInfo t2) {
            return type.compare(t1, t2);
        }
        
        private enum CompareType {
            GAOTIE {
                @Override
                public int compare(TrainInfo t1, TrainInfo t2) {
                    int diff = getInt(t1.getZe_num()) - getInt(t2.getZe_num());//比较二等座
                    if (diff != 0)
                        return diff;
                    
                    diff = getInt(t1.getZy_num()) - getInt(t2.getZy_num());//比较一等座
                    if (diff != 0)
                        return diff;
                    return 0;
                }
            },
            OTHER {
                @Override
                public int compare(TrainInfo t1, TrainInfo t2) {
                    int diff = getInt(t1.getYz_num()) - getInt(t2.getYz_num());//比较硬座
                    if (diff != 0)
                        return diff;
                    
                    diff = getInt(t1.getRz_num()) - getInt(t2.getRz_num());//比较软座
                    if (diff != 0)
                        return diff;
                    
                    diff = getInt(t1.getYw_num()) - getInt(t2.getYw_num());//比较硬卧
                    if (diff != 0)
                        return diff;
                    
                    diff = getInt(t1.getRw_num()) - getInt(t2.getRw_num());//比较软卧
                    if (diff != 0)
                        return diff;
                    return 0;
                }
            };
            abstract int compare(TrainInfo t1, TrainInfo t2);
        }
        
        private static int getInt(String s) {
            if (TrainInfo.isSellOut(s)) {
                return 0;
            }
            else if (s.equals("有")) {
                return Integer.MAX_VALUE;
            }
            else {
                int i = Integer.valueOf(s);
                return i;
            }
        }
        
        public String getLabel() {
            return label;
        };
    }
    
    private TrainType getTrainType(String trainCode) {
        TrainType type = TrainType.QITA;
        if (StringUtils.isEmpty(trainCode))
            return type;
        
        char firstChar = trainCode.charAt(0);
        switch (firstChar) {
            case 'D':
            case 'C':
            case 'G':
                type = TrainType.GAOTIE;
                break;
            case 'Z':
                type = TrainType.ZHIDA;
                break;
            case 'T':
                type = TrainType.TEKUAI;
                break;
            case 'K':
                type = TrainType.KUAISU;
                break;
            case 'L':
                type = TrainType.LINKE;
                break;
            default:
                break;
        }
        return type;
    }
    
}
