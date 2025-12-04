package com.jsh.erp.datasource.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LcAgencyExample {

    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public LcAgencyExample() {
        oredCriteria = new ArrayList<>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.isEmpty()) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        return new Criteria();
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }


    // ================================================================
    //  Criteria Base Class
    // ================================================================
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            criteria = new ArrayList<>();
        }

        public boolean isValid() {
            return !criteria.isEmpty();
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) throw new RuntimeException("Condition cannot be null");
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) throw new RuntimeException("Value for " + property + " cannot be null");
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object v1, Object v2, String property) {
            if (v1 == null || v2 == null)
                throw new RuntimeException("Between values for " + property + " cannot be null");
            criteria.add(new Criterion(condition, v1, v2));
        }


        // ================================================================
        //  Fields for LC_Agency
        // ================================================================

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andEmailLike(String value) {
            addCriterion("email like", value, "email");
            return (Criteria) this;
        }

        public Criteria andPhoneLike(String value) {
            addCriterion("phone like", value, "phone");
            return (Criteria) this;
        }

        public Criteria andCityLike(String value) {
            addCriterion("city like", value, "city");
            return (Criteria) this;
        }

        public Criteria andStateLike(String value) {
            addCriterion("state like", value, "state");
            return (Criteria) this;
        }

        public Criteria andPostalEqualTo(String value) {
            addCriterion("postal =", value, "postal");
            return (Criteria) this;
        }

        public Criteria andEnrollmentLike(String value) {
            addCriterion("enrollment like", value, "enrollment");
            return (Criteria) this;
        }

        public Criteria andWebsiteLike(String value) {
            addCriterion("website like", value, "website");
            return (Criteria) this;
        }

        public Criteria andTierEqualTo(String value) {
            addCriterion("tier =", value, "tier");
            return (Criteria) this;
        }

        public Criteria andEinEqualTo(String value) {
            addCriterion("EIN =", value, "EIN");
            return (Criteria) this;
        }

        public Criteria andMakeTimeBetween(Date v1, Date v2) {
            addCriterion("make_time between", v1, v2, "makeTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeBetween(Date v1, Date v2) {
            addCriterion("modify_time between", v1, v2, "modifyTime");
            return (Criteria) this;
        }
    }


    // ================================================================
    //  Actual Criteria Class
    // ================================================================
    public static class Criteria extends GeneratedCriteria {
        protected Criteria() { super(); }
    }

    // ================================================================
    //  Criterion Class
    // ================================================================
    public static class Criterion {
        private final String condition;
        private final Object value;
        private final Object secondValue;
        private final boolean noValue;
        private final boolean singleValue;
        private final boolean betweenValue;
        private final boolean listValue;

        public String getCondition() { return condition; }
        public Object getValue() { return value; }
        public Object getSecondValue() { return secondValue; }

        protected Criterion(String condition) {
            this.condition = condition;
            this.value = null;
            this.secondValue = null;
            this.noValue = true;
            this.singleValue = false;
            this.betweenValue = false;
            this.listValue = false;
        }

        protected Criterion(String condition, Object value) {
            this.condition = condition;
            this.value = value;
            this.secondValue = null;
            this.noValue = false;
            this.singleValue = !(value instanceof List<?>);
            this.listValue = value instanceof List<?>;
            this.betweenValue = false;
        }

        protected Criterion(String condition, Object v1, Object v2) {
            this.condition = condition;
            this.value = v1;
            this.secondValue = v2;
            this.noValue = false;
            this.singleValue = false;
            this.listValue = false;
            this.betweenValue = true;
        }
    }
}
