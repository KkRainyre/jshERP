<template>
  <div class="agency-page">
    <!-- Top Actions / Breadcrumb area -->
    <div class="page-header-actions">
      <div class="header-left">
        <a-button type="link" icon="left" class="back-btn">Contacts</a-button>
        <span class="separator">|</span>
        <span class="contact-name">Brian Halligan</span>
      </div>
      <div class="header-right">
        <a-button class="action-btn">Actions <a-icon type="down" /></a-button>
      </div>
    </div>

    <a-row :gutter="24">
      <!-- Left Sidebar: Contact Info -->
      <a-col :xs="24" :sm="24" :md="7" :lg="6" :xl="6">
        <a-card :bordered="false" class="left-card hover-shadow">
          <div class="profile-header">
            <div class="avatar-wrapper">
              <a-avatar :size="84" style="background-color: #ff7a59; font-size: 32px">BH</a-avatar>
              <div class="online-status"></div>
            </div>
            <div class="profile-info">
              <h3 class="name">Brian Halligan <br/>(Sample Contact)</h3>
              <p class="title">Executive Chairperson at HubSpot</p>
              <p class="email">bh@hubspot.com <a-icon type="copy" class="copy-icon" /></p>
            </div>
          </div>

          <div class="quick-actions">
            <div class="action-item">
              <a-button shape="circle" icon="file-text" size="large" />
              <span>Note</span>
            </div>
            <div class="action-item">
              <a-button shape="circle" icon="mail" size="large" />
              <span>Email</span>
            </div>
            <div class="action-item">
              <a-button shape="circle" icon="phone" size="large" />
              <span>Call</span>
            </div>
            <div class="action-item">
              <a-button shape="circle" icon="check-square" size="large" />
              <span>Task</span>
            </div>
            <div class="action-item">
              <a-button shape="circle" icon="calendar" size="large" />
              <span>Meeting</span>
            </div>
            <div class="action-item">
              <a-button shape="circle" icon="ellipsis" size="large" />
              <span>More</span>
            </div>
          </div>

          <a-divider style="margin: 12px 0" />

          <a-collapse defaultActiveKey="1" :bordered="false" expandIconPosition="right">
            <a-collapse-panel header="Key information" key="1" class="custom-panel">
              <div class="info-row">
                <label>Contact owner</label>
                <div class="value text-muted">No owner</div>
              </div>
              <div class="info-row">
                <label>Lead Status</label>
                <div class="value">--</div>
              </div>
              <div class="info-row">
                <label>Lifecycle Stage</label>
                <div class="value">Lead</div>
              </div>
              <div class="info-row">
                <label>Lost Contacted</label>
                <div class="value">12/02/2025 6:30 PM EST</div>
              </div>
            </a-collapse-panel>
          </a-collapse>
        </a-card>
      </a-col>

      <!-- Center Content: Activity Feed -->
      <a-col :xs="24" :sm="24" :md="10" :lg="12" :xl="12">
        <a-card :bordered="false" class="center-card hover-shadow" :bodyStyle="{ padding: '0' }">
          <a-tabs defaultActiveKey="1" class="custom-tabs" :tabBarStyle="{ padding: '0 16px', marginBottom: '0' }">
            <a-tab-pane key="1" tab="Agent">
              <div class="tab-content">
                <!-- Breeze Summary -->
                <div class="breeze-summary">
                  <div class="summary-header">
                    <h4>Breeze record summary</h4>
                    <a-tag color="#ff7a59" style="color: white; border: none;">AI</a-tag>
                  </div>
                  <div class="summary-body">
                    <p>
                      The most recent activity is a call with Brian Halligan on December 2, 2025, where interest was expressed in the extra tasty cupcake option and a meeting was scheduled for next week [SEARCH_RESULT_2]. Prior to that, a task was created to follow up with Brian next Tuesday about cupcake models [SEARCH_RESULT_3], and an email was sent on November 30, 2025, introducing the company's cupcake offerings [SEARCH_RESULT_4]. Additionally, a meeting is scheduled for December 5, 2025.
                    </p>
                    <a-button size="small" icon="plus" class="ask-btn">Ask a question</a-button>
                  </div>
                </div>

                <!-- Contact Profile -->
                <div class="section-block">
                  <div class="section-header">
                    <h4>Contact profile</h4>
                    <a-icon type="setting" />
                  </div>
                  <a-row :gutter="16">
                    <a-col :span="12">
                      <div class="field-group">
                        <label>Company name</label>
                        <div class="field-value">HubSpot</div>
                      </div>
                      <div class="field-group">
                        <label>State/Region</label>
                        <div class="field-value">--</div>
                      </div>
                    </a-col>
                    <a-col :span="12">
                      <div class="field-group">
                        <label>Street address</label>
                        <div class="field-value">--</div>
                      </div>
                      <div class="field-group">
                        <label>Email</label>
                        <div class="field-value">bh@hubspot.com</div>
                      </div>
                    </a-col>
                    <a-col :span="12">
                      <div class="field-group">
                        <label>City</label>
                        <div class="field-value">Cambridge</div>
                      </div>
                    </a-col>
                    <a-col :span="12">
                      <div class="field-group">
                        <label>Postal code</label>
                        <div class="field-value">--</div>
                      </div>
                    </a-col>
                  </a-row>
                </div>

                <!-- Enrollments -->
                <div class="section-block">
                  <div class="section-header">
                    <h4>Enrollments</h4>
                    <a-icon type="down" />
                  </div>
                </div>

                <!-- Communication subscriptions -->
                <div class="section-block">
                   <div class="section-header">
                    <h4>Communication subscriptions</h4>
                  </div>
                  <p class="sub-text">Use subscription types to manage the communications this contact receives from you.</p>
                  <a href="#">View subscriptions</a>
                </div>

              </div>
            </a-tab-pane>
            <a-tab-pane key="2" tab="Projects">
              <div class="tab-content">Activities Content</div>
            </a-tab-pane>
            <a-tab-pane key="3" tab="Sales Activty">
              <div class="tab-content">Revenue Content</div>
            </a-tab-pane>
            <a-tab-pane key="4" tab="Marketing">
              <div class="tab-content">Intelligence Content</div>
            </a-tab-pane>
            <a-tab-pane key="4" tab="Intelligence">
              <div class="tab-content">Intelligence Content</div>
            </a-tab-pane>
          </a-tabs>
        </a-card>
      </a-col>

      <!-- Right Sidebar: Associations -->
      <a-col :xs="24" :sm="24" :md="7" :lg="6" :xl="6">
        <a-card :bordered="false" class="right-card hover-shadow">
          <a-collapse defaultActiveKey="1" :bordered="false" expandIconPosition="left">
            <a-collapse-panel key="1" class="right-panel">
              <template slot="header">
                <span class="panel-title">Companies (1)</span>
              </template>
              <a-icon slot="extra" type="plus" class="add-icon" />
              
              <div class="company-item">
                <a-avatar shape="square" size="small" style="background-color: #ff7a59">H</a-avatar>
                <div class="company-details">
                  <div class="company-name">HubSpot <a-tag color="cyan" style="font-size: 10px; line-height: 12px; height: 16px; padding: 0 4px;">Primary</a-tag></div>
                  <div class="company-domain">Company Domain Name: hubspot.com <a-icon type="link" /></div>
                  <div class="company-phone">Phone: --</div>
                  <a href="#" class="assoc-label">Add association label</a>
                </div>
              </div>
              <a-button type="link" size="small" style="padding-left: 0">View all associated Companies</a-button>
            </a-collapse-panel>

            <a-collapse-panel key="2" class="right-panel">
               <template slot="header">
                <span class="panel-title">Deals (0)</span>
              </template>
              <a-icon slot="extra" type="plus" class="add-icon" />
              <div class="empty-state">
                <img src="https://static.hsappstatic.net/ui-images/static-2.427/optimized/empty-state-charts.svg" width="60" style="opacity: 0.5; margin-bottom: 10px;" />
                <p>Track the revenue opportunities associated with this record.</p>
              </div>
            </a-collapse-panel>

            <a-collapse-panel key="3" class="right-panel">
               <template slot="header">
                <span class="panel-title">Tickets (0)</span>
              </template>
              <a-icon slot="extra" type="plus" class="add-icon" />
              <div class="empty-state">
                 <img src="https://static.hsappstatic.net/ui-images/static-2.427/optimized/empty-state-charts.svg" width="60" style="opacity: 0.5; margin-bottom: 10px;" />
                <p>Track the customer requests associated with this record.</p>
              </div>
            </a-collapse-panel>

             <a-collapse-panel key="4" class="right-panel">
               <template slot="header">
                <span class="panel-title">Attachments</span>
              </template>
              <a-icon slot="extra" type="plus" class="add-icon" />
            </a-collapse-panel>

          </a-collapse>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>
export default {
  name: 'Agency',
  data() {
    return {
    }
  },
  methods: {
  }
}
</script>

<style scoped lang="less">
.agency-page {
  padding: 16px;
  background-color: #f5f8fa;
  min-height: calc(100vh - 64px);
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
}

.hover-shadow {
  transition: box-shadow 0.3s;
  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }
}

/* Header */
.page-header-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  
  .header-left {
    display: flex;
    align-items: center;
    .back-btn {
      color: #33475b;
      font-weight: 600;
      padding-left: 0;
      &:hover {
        color: #ff7a59;
      }
    }
    .separator {
      margin: 0 8px;
      color: #cbd6e2;
    }
    .contact-name {
      font-weight: 600;
      font-size: 16px;
      color: #33475b;
    }
  }
  
  .action-btn {
    border-color: #ff7a59;
    color: #ff7a59;
    &:hover {
      background-color: #fff1ef;
    }
  }
}

/* Left Card */
.left-card {
  border-radius: 8px;
  margin-bottom: 16px;
  
  .profile-header {
    text-align: center;
    margin-bottom: 24px;
    
    .avatar-wrapper {
      position: relative;
      display: inline-block;
      margin-bottom: 12px;
    }
    
    .name {
      font-size: 20px;
      font-weight: 700;
      color: #33475b;
      margin-bottom: 4px;
      line-height: 1.2;
    }
    .title {
      color: #516f90;
      margin-bottom: 4px;
    }
    .email {
      color: #516f90;
      .copy-icon {
        cursor: pointer;
        margin-left: 4px;
        &:hover { color: #ff7a59; }
      }
    }
  }
  
  .quick-actions {
    display: flex;
    justify-content: space-between;
    flex-wrap: wrap;
    margin-bottom: 16px;
    
    .action-item {
      text-align: center;
      width: 33%;
      margin-bottom: 12px;
      
      .ant-btn {
        border-color: #cbd6e2;
        color: #516f90;
        margin-bottom: 4px;
        &:hover {
          border-color: #ff7a59;
          color: #ff7a59;
        }
      }
      span {
        display: block;
        font-size: 12px;
        color: #516f90;
      }
    }
  }
}

/* Center Card */
.center-card {
  border-radius: 8px;
  margin-bottom: 16px;
  min-height: 600px;
  
  .custom-tabs {
    /deep/ .ant-tabs-nav .ant-tabs-tab {
      margin: 0 16px 0 0;
      padding: 12px 0;
      font-weight: 500;
      &:hover {
        color: #ff7a59;
      }
    }
    /deep/ .ant-tabs-nav .ant-tabs-tab-active {
      color: #33475b;
      font-weight: 600;
      border-bottom: 3px solid #ff7a59;
    }
    /deep/ .ant-tabs-ink-bar {
      display: none; /* Hide default ink bar to use border-bottom instead for custom look */
    }
  }
  
  .tab-content {
    padding: 20px;
  }
  
  .breeze-summary {
    background: #fff5f5;
    border: 1px solid #ffdfdf;
    border-radius: 6px;
    padding: 16px;
    margin-bottom: 24px;
    
    .summary-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 12px;
      h4 {
        font-weight: 700;
        margin: 0;
        color: #33475b;
      }
    }
    .summary-body {
      color: #33475b;
      font-size: 14px;
      line-height: 1.6;
      
      .ask-btn {
        margin-top: 12px;
        border-radius: 12px;
        border-color: #cbd6e2;
        color: #516f90;
        &:hover {
          border-color: #ff7a59;
          color: #ff7a59;
        }
      }
    }
  }
  
  .section-block {
    border: 1px solid #eaf0f6;
    border-radius: 6px;
    padding: 16px;
    margin-bottom: 24px;
    
    .section-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;
      h4 {
        font-weight: 700;
        margin: 0;
        color: #33475b;
      }
    }
    
    .field-group {
      margin-bottom: 16px;
      label {
        display: block;
        font-size: 12px;
        color: #516f90;
        margin-bottom: 4px;
      }
      .field-value {
        color: #33475b;
        font-size: 14px;
      }
    }
    
    .sub-text {
      color: #516f90;
      font-size: 13px;
      margin-bottom: 8px;
    }
  }
}

/* Right Card */
.right-card {
  border-radius: 8px;
  margin-bottom: 16px;
  
  .right-panel {
    border-bottom: 1px solid #eaf0f6;
    
    .panel-title {
      font-weight: 600;
      color: #33475b;
    }
    
    .add-icon {
      color: #ff7a59;
      cursor: pointer;
    }
    
    .company-item {
      display: flex;
      align-items: flex-start;
      margin-bottom: 12px;
      margin-top: 8px;
      
      .company-details {
        margin-left: 10px;
        flex: 1;
        
        .company-name {
          font-weight: 600;
          color: #33475b;
          display: flex;
          align-items: center;
          gap: 4px;
        }
        .company-domain, .company-phone {
          font-size: 12px;
          color: #516f90;
          margin-top: 2px;
        }
        .assoc-label {
          font-size: 12px;
          display: block;
          margin-top: 4px;
        }
      }
    }
    
    .empty-state {
      text-align: center;
      padding: 16px 0;
      color: #516f90;
      font-size: 13px;
    }
  }
}

/* Global Overrides */
/deep/ .ant-collapse-item {
  border-bottom: 1px solid #eaf0f6;
}
/deep/ .ant-collapse-header {
  padding: 12px 0 !important;
  color: #33475b !important;
}
/deep/ .ant-collapse-content-box {
  padding: 8px 0 16px !important;
}

.info-row {
  margin-bottom: 12px;
  label {
    display: block;
    font-size: 12px;
    color: #516f90;
  }
  .value {
    color: #33475b;
    font-size: 14px;
  }
  .text-muted {
    color: #99acc2;
    font-style: italic;
  }
}
</style>