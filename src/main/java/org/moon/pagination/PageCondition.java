package org.moon.pagination;

import com.google.common.base.Strings;
import org.moon.utils.Objects;
import org.moon.utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分页参数
 * @author:Gavin
 * @date 2015/5/5 0005
 */
public class PageCondition {

    private int offset = 0,
            pageIndex  = 1,
            limit = 15;


    private String sort;//排序字符串

    private PageCondition(){}

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public static class Builder{
        private List<Pair<String,String>> pairs = new ArrayList<>();
        private String ASC = "ASC",DESC = "DESC";
        private PageCondition pageCondition = new PageCondition();
        public Builder offset(int offset){
            if(offset>0) {
                pageCondition.offset = offset;
            }
            return this;
        }

        public Builder pageIndex(int pageIndex){
            if(pageIndex>0) {
                pageCondition.pageIndex = pageIndex;
            }
            return this;
        }
        public Builder limit(int limit){
            if(limit>0){
                pageCondition.limit = limit;
            }
            return this;
        }

        public Builder asc(String sortColumn){
            if(Objects.nonNull(sortColumn)){
                pairs.add(new Pair<>(sortColumn,ASC));
            }
            return this;
        }

        public Builder desc(String sortColumn){
            if(Objects.nonNull(sortColumn)){
                pairs.add(new Pair<>(sortColumn,DESC));
            }
            return this;
        }

        public PageCondition build(){
            pageCondition.sort = pairs.stream().map(pair->pair.getKey()+" "+pair.getValue())
                    .collect(Collectors.joining(","));
            if(Strings.isNullOrEmpty(pageCondition.sort)){//默认order by 1
                pageCondition.sort = " 1 ";
            }
            if(pageCondition.offset >0){
                pageCondition.pageIndex = (pageCondition.offset+1)/pageCondition.limit + ((pageCondition.offset+1)%pageCondition.limit>0?1:0);
                if(pageCondition.pageIndex < 0){
                    pageCondition.pageIndex = 1;
                }
            }else if(pageCondition.offset == 0 && pageCondition.pageIndex > 0){
                pageCondition.offset = (pageCondition.pageIndex-1)*pageCondition.limit;
            }
            return pageCondition;
        }
    }

}
