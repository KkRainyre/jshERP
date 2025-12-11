<template>
  <div class="agency-page">
    <!-- Top Actions / Breadcrumb area -->
    <div class="page-header-actions">
      <div class="header-left">
        <a-button type="link" icon="left" class="back-btn" @click="handleBack">Back</a-button>
        <span class="separator">|</span>
        <span class="contact-name">{{ agency.name }}</span>
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
              <a-avatar
                :size="84"
                v-if="agency.logo"
                :src="'data:image/jpeg;base64,' + agency.logo"
              />

              <a-avatar
                :size="84"
                v-else
                style="background-color: #ff7a59; font-size: 32px"
              >
                {{ agencyInitial }}
              </a-avatar>
              <div class="online-status"></div>
            </div>
            <div class="profile-info">
              <h3 class="name">{{agency.name}} <br/>(Sample Company)</h3>
              <p class="title">{{agency.website}}</p>
              <p class="email">
                <span @click="openEmail">{{ agency.email }}</span>
                <a-icon type="copy" class="copy-icon" @click.stop="copyEmail" />
              </p>
            </div>
          </div>

          <div class="quick-actions">
            <div class="action-item">
              <a-button shape="circle" icon="file-text" size="large" @click.native="handleOpenNotes" />
              <span>Note</span>
            </div>
            <div class="action-item">
              <a-button shape="circle" icon="mail" size="large" @click.native="openEmail" />
              <span>Email</span>
            </div>
            <div class="action-item">
              <a-button shape="circle" icon="phone" size="large" @click.native="handleCallPhone" />
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
                <label>Tier</label>
                <div class="value text-muted">{{agency.tier}}</div>
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
                <label>Last Contacted</label>
                <div class="value">{{agency.modify_time}}</div>
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

                <div class="section-block">
                  <div class="record-table-list">

                    <a-table
                      size="small"
                      rowKey="id"
                      :columns="listColumns"
                      :dataSource="activities"
                      :pagination="{ pageSize: 5 }"
                      :loading="loading"
                      :scroll="{ x: 1300 }"
                    >
                      <!-- Logo Render -->
                      <template slot="logo" slot-scope="text">
                         <img v-if="text" :src="'data:image/jpeg;base64,' + text" style="width: 30px; height: 30px; object-fit: contain;" />
                      </template>
                      <!-- Action -->
                      <template slot="action" slot-scope="text, record">
                        <a @click="handleEdit(record)">edit</a>
                        <a-divider type="vertical" />
                        <a-popconfirm title="Sure to delete?" @confirm="handleDelete(record.id)">
                          <a style="color: red;">delete</a>
                        </a-popconfirm>
                      </template>
                    </a-table>

                    <lcagent-modal ref="lcagentModal" @saved="loadActivities" />
                    <import-file-modal ref="modalImportForm" @ok="loadActivities"></import-file-modal>
                  </div>
                </div>

                  <div class="section-block">


                  <div class="section-header">
                    <h4>Contact profile</h4>
                    <a-icon type="setting" />
                  </div>
                  <a-row :gutter="16">
                    <a-col :span="12">
                      <div class="field-group">
                        <label>Company name</label>
                        <div class="field-value">{{agency.name}}</div>
                      </div>
                      <div class="field-group">
                        <label>State/Region</label>
                        <div class="field-value">{{ agency.state }}</div>
                      </div>
                    </a-col>
                    <a-col :span="12">
                      <div class="field-group">
                        <label>Street address</label>
                        <div class="field-value">{{ agency.address }}</div>
                      </div>
                      <div class="field-group">
                        <label>Email</label>
                        <div class="field-value">{{ agency.email }}</div>
                      </div>
                    </a-col>
                    <a-col :span="12">
                      <div class="field-group">
                        <label>City</label>
                        <div class="field-value">{{ agency.city }}</div>
                      </div>
                    </a-col>
                    <a-col :span="12">
                      <div class="field-group">
                        <label>Postal code</label>
                        <div class="field-value">{{ agency.postal }}</div>
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
                <span class="panel-title">Companies </span>
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


    <!-- Notes Modal -->
    <a-modal
      title="Notes"
      :visible="notesVisible"
      @ok="handleSaveNotes"
      @cancel="notesVisible = false"
      :maskClosable="false"
      width="600px"
    >
      <a-textarea 
        v-model="notesContent" 
        :rows="12" 
        placeholder="Enter notes here..." 
        style="resize: none; border: 1px solid #e8e8e8; background: #fafafa; padding: 12px; border-radius: 4px;"
      />
    </a-modal>
  </div>
</template>

<script>
import { getAction, deleteAction } from '@/api/manage'
import { editAgency } from '@/api/api'
import ImportFileModal from "@comp/tools/ImportFileModal.vue";
import LcagentModal from "./modules/LcagentModal.vue";

export default {
  name: 'agent-overview',
  components: {ImportFileModal, LcagentModal},
  data() {
    return {
      agencyId: null,
      loading: false,
      agency: {},
      activities: [],
      notesVisible: false,
      notesContent: '',
      currentIndex: 0,
      total: 0,
      listColumns: [
        { title: 'Avatar', dataIndex: 'logo', scopedSlots: { customRender: 'logo' }, align:"center", width: 60 },
        { title: 'Name', dataIndex: 'name', width: 150 , align:"center"},
        { title: 'Phone', dataIndex: 'phone', width: 100 , align:"center"},
        { title: 'Email', dataIndex: 'email', width: 150 , align:"center"},
        { title: 'Shipping', dataIndex: 'shipping', width: 80 , align:"center"},
        { title: 'Currency', dataIndex: 'currency', width: 80 , align:"center"},
        { title: 'Category', dataIndex: 'category', width: 80 , align:"center"},
        { title: 'Remark', dataIndex: 'remark', width: 150 , align:"center"},
      ]
    }
  },
  created() {

    this.agencyId = this.$route.query.id || this.$route.params.id;
    console.log("Agency ID:", this.agencyId);

    this.loadAgency();
    this.loadActivities();
    console.log('editAgency loaded:', !!editAgency);
  },
  methods: {
    async loadAgency() {
      if (!this.agencyId) return;
      const res = await getAction('/agency/info', { id: this.agencyId });
      if (res && res.code === 200 && res.data && res.data.info) {
        this.agency = res.data.info;
      }
    },
    async loadActivities() {
      this.loading = true;
      try {
        const res = await getAction(`/agency/agent`, { id: this.agencyId });
        if (res && res.data) {
          this.activities = Array.isArray(res.data.records) ? res.data.records : (Array.isArray(res.data) ? res.data : []);
          this.total = this.activities.length;
        }
      } catch (e) {
        console.error("Failed to load detail info", e);
        this.activities = [];
      } finally {
        this.loading = false
      }
    },
    
    modalFormOk() {
      this.loadActivities();
    },

    handleBack() {
      this.$router.push({ name: 'Agency' });
    },

    handleAdd() {
      this.$refs.lcagentModal.open(null, { companyId: this.agencyId });
    },

    handleEdit(record) {
      this.$refs.lcagentModal.open(record);
    },

    async handleDelete(id) {
       await deleteAction(`/lcagent/delete`, { ids: id });
       this.$message.success('Deleted');
       this.loadActivities();
    },

    copyEmail() {
      if (this.agency.email) {
        navigator.clipboard.writeText(this.agency.email).then(() => {
          this.$message.success('Email copied to clipboard');
        }).catch(err => {
          this.$message.error('Failed to copy email');
        });
      }
    },

    openEmail() {
      if (this.agency.email) {
        window.location.href = `mailto:${this.agency.email}`;
      }
    },

    handleCallPhone() {
      // Debug message
      this.$message.info('Debug: Call button clicked');
      
      if (this.agency.phone) {
        // Copy to clipboard first so user has it if call fails
        navigator.clipboard.writeText(this.agency.phone).then(() => {
          this.$message.success(`Phone number copied: ${this.agency.phone}`);
        }).catch(() => {});

        // Attempt to open default calling app
        window.location.href = `tel:${this.agency.phone}`;
      } else {
        this.$message.warning('No phone number available');
      }
    },

    handleOpenNotes() {
      // Debug message
      this.$message.info('Debug: Note button clicked');
      console.log('openNotes clicked');
      console.log('Current remarks:', this.agency.remarks);
      this.notesContent = this.agency.remarks || '';
      this.notesVisible = true;
      console.log('notesVisible set to:', this.notesVisible);
    },

    async handleSaveNotes() {
       const formData = { ...this.agency, remarks: this.notesContent };
       
       try {
         const res = await editAgency(formData);
         if (res && res.code === 200) {
            this.$message.success('Notes saved');
            this.agency.remarks = this.notesContent; // Update local state
            this.notesVisible = false;
         } else {
            this.$message.warning(res.data.message || 'Failed to save notes');
         }
       } catch(e) {
         console.error(e);
         this.$message.error('Error saving notes');
       }
    }
  }
  ,
  computed: {
    agencyInitial() {
      return this.agency && this.agency.name
        ? this.agency.name.charAt(0).toUpperCase()
        : "A";
    }
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
      cursor: pointer;
      &:hover { color: #ff7a59; }
      .copy-icon {
        cursor: pointer;
        margin-left: 4px;
        color: #516f90;
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

    .record-pagination {
      display: flex;
      align-items: center;
      margin-bottom: 16px;
      
      .page-indicator {
        display: flex;
        align-items: center;
        margin: 0 8px;
        font-size: 13px;
        
        .current {
          border: 1px solid #cbd6e2;
          background-color: #fff;
          border-radius: 3px;
          padding: 2px 8px;
          color: #33475b;
          margin-right: 4px;
          min-width: 32px;
          text-align: center;
        }
        .divider, .total {
           margin: 0 2px;
           color: #7c98b6;
        }
      }
      
      .ant-btn {
        color: #7c98b6;
        padding: 0 4px;
        height: auto;
        line-height: 1;
        border: none;
        box-shadow: none;
        
        &:hover {
          color: #ff7a59;
          background: transparent;
        }
        &[disabled] {
          color: #dfe3eb;
          background: transparent;
        }
      }
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

    /deep/ .ant-collapse-header {
      padding: 12px 16px 12px 32px !important;
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