package org.orrs.model;

import java.util.List;

public class DocumentSetting {
    private String enable;
    private List<Setting> setting;

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public List<Setting> getSetting() {
        return setting;
    }

    public void setSetting(List<Setting> setting) {
        this.setting = setting;
    }

    public static class Setting {
        private String memo;
        private String variable;
        private String sqlCondition;
        private String test;

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        public String getVariable() {
            return variable;
        }

        public void setVariable(String variable) {
            this.variable = variable;
        }
        
        public String getSqlCondition() {
			return sqlCondition;
		}

		public void setSqlCondition(String sqlCondition) {
			this.sqlCondition = sqlCondition;
		}

		public String getTest() {
            return test;
        }

        public void setTest(String test) {
            this.test = test;
        }
    }
}
