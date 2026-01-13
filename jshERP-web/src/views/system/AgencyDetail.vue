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
              <div 
                v-if="agency.logo" 
                class="logo-rect-container"
              >
                <img
                  :src="agencyLogoUrl || 'data:image/jpeg;base64,' + agency.logo"
                  class="hover-zoom-img"
                  style="width: 100%; height: 100%; object-fit: contain"
                  @error="$event.target.src='data:image/jpeg;base64,' + (agency.logoThumb || agency.logo)"
                />
              </div>

              <a-avatar
                :size="120"
                shape="square"
                v-else
                style="background-color: #ff7a59; font-size: 48px"
              >
                {{ agencyInitial }}
              </a-avatar>
              
              <!-- Large Preview on Hover -->
              <div class="logo-large-preview" v-if="agency.logo">
                <img :src="agencyLogoUrl || 'data:image/jpeg;base64,' + agency.logo" style="width: 100%;" />
              </div>
              
              <div class="online-status"></div>
            </div>
            <div class="profile-info">
              <h3 class="name">{{agency.name}} <br/></h3>
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
              <a-button shape="circle" icon="check-square" size="large" @click="handleOpenTasks" />
              <span>Task</span>
            </div>
            <div class="action-item">
              <a-button shape="circle" icon="calendar" size="large" @click="handleMeeting" />
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
                  <div class="section-header">
                    <h4>Agents</h4>
                    <a-button type="primary" size="small" icon="plus" @click="handleAdd">Add Agent</a-button>
                  </div>
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
                        <a-popconfirm title="Sure to delete?" okText="Confirm" @confirm="handleDelete(record.id)">
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
              <div class="tab-content">
                <div class="section-block">
                  <div class="section-header">
                     <h4>Projects</h4>
                     <a-button type="primary" size="small" icon="plus" @click="handleAddProject">Add Project</a-button>
                  </div>
                  <div class="record-table-list">
                    <a-table
                      size="small"
                      rowKey="id"
                      :columns="projectColumns"
                      :dataSource="projects"
                      :pagination="{ pageSize: 5 }"
                      :loading="loadingProjects"
                      :scroll="{ x: 1000 }"
                    >
                      <!-- Project Name -->
                      <template slot="projectName" slot-scope="text, record">
                        <a @click="handleProjectDetail(record)">{{ text }}</a>
                      </template>
                      <!-- STATUS -->
                      <template slot="status" slot-scope="text">
                        <a-tag :color="getStatusColor(text)">{{ text }}</a-tag>
                      </template>
                      <!-- ACTION -->
                      <template slot="projectAction" slot-scope="text, record">
                        <a @click="handleEditProject(record)">edit</a>
                         <a-divider type="vertical" />
                        <a-popconfirm title="Sure to delete?" okText="Confirm" @confirm="handleDeleteProject(record.id)">
                          <a style="color: red;">delete</a>
                        </a-popconfirm>
                      </template>
                    </a-table>

                    <lcproject-modal ref="lcprojectModal" @saved="projectModalOk" />
                  </div>
                </div>
              </div>
            </a-tab-pane>
            <a-tab-pane key="3" tab="Sales Activty">
              <div class="tab-content">Revenue Content</div>
            </a-tab-pane>
            <a-tab-pane key="4" tab="Marketing">
              <div class="tab-content">Intelligence Content</div>
            </a-tab-pane>
            <a-tab-pane key="5" tab="Intelligence">
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
      okText="Confirm"
      cancelText="Cancel"
    >
      <a-textarea 
        v-model="notesContent" 
        :rows="12" 
        placeholder="Enter notes here..." 
        style="resize: none; border: 1px solid #e8e8e8; background: #fafafa; padding: 12px; border-radius: 4px;"
      />
    </a-modal>

    <!-- Tasks Modal -->
    <a-modal
      title="Tasks"
      :visible="tasksVisible"
      @cancel="tasksVisible = false"
      :footer="null"
      width="600px"
    >
      <div class="task-input-row" style="display: flex; gap: 8px; margin-bottom: 16px;">
        <a-input v-model="newTaskTitle" placeholder="Task title..." style="flex: 1;" @keyup.enter="addTask" />
        <a-date-picker v-model="newTaskDate" placeholder="Due date" />
        <a-button type="primary" icon="plus" @click="addTask">Add</a-button>
      </div>
      
      <a-list :dataSource="taskList" itemLayout="horizontal">
        <a-list-item slot="renderItem" slot-scope="item, index">
          <a-checkbox slot="actions" :checked="item.completed" @change="toggleTask(item)"></a-checkbox>
          <a slot="actions" style="color: red" @click="removeTask(item)"><a-icon type="delete" /></a>
          
          <a-list-item-meta :description="item.dueDate ? 'Due: ' + item.dueDate : ''">
             <span slot="title" :style="{ textDecoration: item.completed ? 'line-through' : 'none', color: item.completed ? '#aaa' : '#333' }">
               {{ item.title }}
             </span>
          </a-list-item-meta>
        </a-list-item>
        <div v-if="taskList.length === 0" style="text-align: center; padding: 20px; color: #999;">
          No tasks yet.
        </div>
      </a-list>
    </a-modal>

    <!-- Meeting Modal -->
    <a-modal
      title="Start Meeting"
      :visible="meetingVisible"
      @cancel="meetingVisible = false"
      :footer="null"
      width="400px"
    >
      <div style="display: flex; flex-direction: column; gap: 16px; padding: 10px;">
        <a-button block size="large" icon="video-camera" @click="launchZoom" style="height: 50px; background: #2D8CFF; color: white; border: none;">
           Launch Zoom Meeting
        </a-button>
        
        <a-button block size="large" icon="windows" @click="launchTeams" style="height: 50px; background: #6264A7; color: white; border: none;">
           Microsoft Teams
        </a-button>
        
        <a-button block size="large" icon="mobile" @click="launchFaceTime" style="height: 50px; background: #34C759; color: white; border: none;">
           FaceTime
        </a-button>
      </div>
    </a-modal>

  </div>
</template>

<script>
import { getAction, deleteAction } from '@/api/manage'
import { editAgency } from '@/api/api'
import ImportFileModal from "@comp/tools/ImportFileModal.vue";
import LcagentModal from "./modules/LcagentModal.vue";
import LcprojectModal from "./modules/LcprojectModal.vue";
import moment from 'moment';

export default {
  name: 'agent-overview',
  components: {ImportFileModal, LcagentModal, LcprojectModal},
  data() {
    return {
      agencyId: null,
      loading: false,
      agency: {},
      agencyLogoUrl: '',
      activities: [],
      notesVisible: false,
      notesContent: '',
      tasksVisible: false,
      taskList: [],
      newTaskTitle: '',
      newTaskDate: null,
      meetingVisible: false,
      currentIndex: 0,
      total: 0,
      listColumns: [
        { title: 'Avatar', dataIndex: 'logo', scopedSlots: { customRender: 'logo' }, align:"center", width: 60 },
        { title: 'Name', dataIndex: 'name', width: 150 , align:"center"},
        { title: 'Title', dataIndex: 'title', width: 100, align:"center" },
        { title: 'Phone', dataIndex: 'phone', width: 100 , align:"center"},
        { title: 'Office Phone', dataIndex: 'officeNum', width: 100, align:"center" },
        { title: 'Extension', dataIndex: 'phoneExt', width: 80, align:"center" },
        { title: 'Email', dataIndex: 'email', width: 150 , align:"center"},
        { title: 'Action', scopedSlots: { customRender: 'action' }, width: 120, align: 'center' }
      ],
      projects: [],
      loadingProjects: false,
      projectColumns: [
        { title: 'Project Name', dataIndex: 'name', width: 150, scopedSlots: { customRender: 'projectName' } },
        { title: 'Agency', dataIndex: 'agency', width: 150 },
        { title: 'Status', dataIndex: 'status', width: 100, scopedSlots: { customRender: 'status' } },
        { title: 'Location', dataIndex: 'location', width: 120 },
        { title: 'Start Date', dataIndex: 'startDate', width: 120, customRender: (text) => !text ? "" : (text.length > 10 ? text.substring(0, 10) : text) },
        { title: 'End Date', dataIndex: 'endDate', width: 120, customRender: (text) => !text ? "" : (text.length > 10 ? text.substring(0, 10) : text) },
        { title: 'Action', scopedSlots: { customRender: 'projectAction' }, width: 120, align: 'center' }
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
        this.resolveLogoUrl(); 
        this.loadProjects();
      }
    },

    resolveLogoUrl() {
       if (this.agency.logo && !this.agency.logo.startsWith('http')) {
           // Use OSS API to get consistent previewUrl
           getAction('/api/oss/urls', { objectKey: this.agency.logo, fileName: '' }).then(res => {
              if (res && res.previewUrl) {
                 this.agencyLogoUrl = res.previewUrl;
              } else if (res) {
                 this.agencyLogoUrl = res.previewUrl || res || '';
              }
           }).catch(err => {
              console.error('Failed to get agency logo preview', err);
           })
       } else if (this.agency.logo && this.agency.logo.startsWith('http')) {
           this.agencyLogoUrl = this.agency.logo;
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
      // this.$message.info('Debug: Call button clicked');
      
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
      this.notesContent = this.agency.remarks || '';
      this.notesVisible = true;
    },

    async handleSaveNotes() {
       let finalContent = this.notesContent;
       const originalContent = this.agency.remarks || '';
       
       // If content changed, append date
       if (finalContent !== originalContent) {
          const dateStr = new Date().toLocaleString(); 
          // Append new line and date
          finalContent += `\n[Modified: ${dateStr}]`;
       }

       const formData = { ...this.agency, remarks: finalContent };
       
       try {
         const res = await editAgency(formData);
         if (res && res.code === 200) {
            this.$message.success('Notes saved');
            this.agency.remarks = finalContent; // Update local state
            this.notesVisible = false;
         } else {
            this.$message.warning(res.data.message || 'Failed to save notes');
         }
       } catch(e) {
         console.error(e);
         this.$message.error('Error saving notes');
       }
    },

    // --- Meeting Methods ---
    handleMeeting() {
       this.meetingVisible = true;
    },
    
    launchZoom() {
       // Opens the Zoom web portal to start a meeting (or launch app if installed)
       window.open('https://zoom.us/start', '_blank');
       this.meetingVisible = false;
    },
    
    launchTeams() {
       // Deep link to create a new meeting in Teams
       window.open('https://teams.microsoft.com/l/meeting/new', '_blank');
       this.meetingVisible = false;
    },
    
    launchFaceTime() {
       const contact = this.agency.phone || this.agency.email;
       if (!contact) {
         this.$message.warning('No phone number or email available for this agency.');
         return;
       }
       // FaceTime protocol
       window.location.href = `facetime://${contact}`;
       this.meetingVisible = false;
    },

    // --- Task Methods ---
    handleOpenTasks() {
      this.tasksVisible = true;
      try {
        if (this.agency.EXT5) {
          this.taskList = JSON.parse(this.agency.EXT5);
        } else {
          this.taskList = [];
        }
      } catch (e) {
        this.taskList = [];
      }
    },
    
    addTask() {
      if (!this.newTaskTitle) return;
      
      const task = {
        id: Date.now(),
        title: this.newTaskTitle,
        dueDate: this.newTaskDate ? this.newTaskDate.format('YYYY-MM-DD') : null,
        completed: false
      };
      
      this.taskList.unshift(task); // Add to top
      this.newTaskTitle = '';
      this.newTaskDate = null;
      this.saveTasks();
    },
    
    toggleTask(task) {
      task.completed = !task.completed;
      this.saveTasks();
    },
    
    removeTask(task) {
      this.taskList = this.taskList.filter(t => t.id !== task.id);
      this.saveTasks();
    },
    
    async saveTasks() {
       const jsonStr = JSON.stringify(this.taskList);
       const formData = { ...this.agency, EXT5: jsonStr };
       
       try {
         const res = await editAgency(formData);
         if (res && res.code === 200) {
           this.agency.EXT5 = jsonStr;
         }
       } catch (e) {
         console.error(e);
       }
    },

    async loadProjects() {
      if (!this.agency || !this.agency.name) return;
      this.loadingProjects = true;
      try {
        const res = await getAction('/lcproject/select', { exactClient: this.agency.name });
        if (res && res.data) {
           // Filter strictly on frontend to ensure data isolation
           const allProjects = Array.isArray(res.data) ? res.data : [];
           this.projects = allProjects.filter(p => p.agency === this.agency.name);
        } else {
           this.projects = [];
        }
      } catch (e) {
        console.error("Failed to load projects", e);
        this.projects = [];
      } finally {
        this.loadingProjects = false;
      }
    },

    handleAddProject() {
      if (!this.agency.name) {
        this.$message.warning("Agency name is missing");
        return;
      }
      this.$refs.lcprojectModal.open(null, { 
        clientDisabled: true, 
        clientName: this.agency.name,
        clientId: this.agency.id // Pass agency ID for EXT2
      });
    },

    handleEditProject(record) {
      this.$refs.lcprojectModal.open(record, { clientDisabled: true });
    },

    async handleDeleteProject(id) {
       await deleteAction(`/lcproject/delete/${id}`);
       this.$message.success('Deleted project');
       this.loadProjects();
    },

    handleProjectDetail(record) {
      this.$router.push({ name: 'ProjectDetail', query: { id: record.id, from: 'agencyDetail', agencyId: this.agencyId } });
    },

    projectModalOk() {
      this.loadProjects();
    },

    getStatusColor(status) {
      switch (status) {
        case 'new-lead': return '#56CCF2';
        case 'budget': return '#F2994A';
        case 'specify': return '#BB6BD9';
        case 'quote': return '#6FCF97';
        case 'follow-up': return '#F2C94C';
        case 'po': return '#EB5757';
        case 'active': return 'green'; // Keep for legacy
        case 'completed': return 'blue'; // Keep for legacy
        case 'on-hold': return 'orange'; // Keep for legacy
        case 'cancelled': return 'red'; // Keep for legacy
        default: return 'default';
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

      .logo-rect-container {
        width: 260px;
        height: 140px;
        position: relative;
        overflow: hidden;
        border-radius: 4px; /* Optional rounded corners */
      }

      /* Hover Preview */
      .logo-large-preview {
          visibility: hidden;
          opacity: 0;
          position: absolute;
          top: 0;
          left: 270px;
          width: 400px;
          background: #fff;
          border: 1px solid #ddd;
          box-shadow: 0 4px 12px rgba(0,0,0,0.2);
          z-index: 999;
          padding: 8px;
          transition: 0.2s;
          pointer-events: none;
      }

      &:hover .logo-large-preview {
          visibility: visible;
          opacity: 1;
      }


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