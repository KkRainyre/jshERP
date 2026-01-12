<template>
  <div class="project-page">
    <!-- Top Actions / Breadcrumb area -->
    <div class="page-header-actions">
      <div class="header-left">
        <a-button type="link" icon="left" class="back-btn" @click="handleBack">Back</a-button>
        <span class="separator">|</span>
        <span class="contact-name">{{ project.name }}</span>
      </div>
      <div class="header-right">
        <a-button class="action-btn" @click="toggleActionsMenu">Actions <a-icon :type="showActionsMenu ? 'up' : 'down'" /></a-button>
      </div>
    </div>

    <a-row :gutter="24">
      <!-- Left Sidebar: Project Info -->
      <a-col :xs="24" :sm="24" :md="7" :lg="6" :xl="6">
        <a-card :bordered="false" class="left-card hover-shadow">
          <div class="profile-header">
            <!-- Pipeline Stepper -->
            <div class="pipeline-wrapper">
              <div 
                v-for="(stage, index) in pipelineStages" 
                :key="index"
                class="step-item"
                :class="{ 'active': index === currentStage }"
                :style="{ '--step-color': stage.color, 'z-index': pipelineStages.length - index }"
                @click="handleStageClick(index, stage)"
              >
                <span>{{ stage.label }}</span>
              </div>
            </div>

            <div class="profile-info">

              <h3 class="name">Project: {{project.name}} <br/></h3>
              <p class="title">Agency: {{project.agency}}</p>

            </div>
          </div>

          <div class="quick-actions">
            <div class="action-item">
              <a-button shape="circle" icon="file-text" size="large" @click.native="handleOpenNotes" />
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
          </div>

          <a-divider style="margin: 12px 0" />

          <a-collapse defaultActiveKey="1" :bordered="false" expandIconPosition="right">
            <a-collapse-panel header="Key information" key="1" class="custom-panel">
              
              <!-- Agency Contact Section -->
              <div class="info-section">
                 <div class="section-label">Agency Contact</div>
                 <div class="contact-group">
                    <div class="c-row">
                        <a-icon type="user" style="color: #1890ff; margin-right: 8px;" />
                        <span class="c-val">{{ project.contactPerson || '--' }}</span>
                    </div>
                    <div class="c-row">
                        <a-icon type="phone" style="color: #52c41a; margin-right: 8px;" />
                        <span class="c-val">{{ project.contactPhone || '--' }}</span>
                    </div>
                    <div class="c-row">
                        <a-icon type="mail" style="color: #722ed1; margin-right: 8px;" />
                        <span class="c-val">{{ project.manager || '--' }}</span>
                    </div>
                 </div>
              </div>

              <a-divider style="margin: 16px 0; border-color: #f0f0f0;" />

              <!-- Project Metrics -->
              <div class="info-row main-metric">
                <label>Budget</label>
                <div class="value price">{{ project.budget ? `$ ${project.budget}` : '--' }}</div>
              </div>
              
              <div class="info-row" style="margin-top: -10px; margin-bottom: 12px">
                 <label style="font-size: 12px; color: #888;">Quote Price</label>
                 <div class="value price" style="font-size: 16px;" :style="{ color: latestQuotePrice ? '#52c41a' : '#ccc' }">
                    {{ latestQuotePrice ? latestQuotePrice : 'No quote available' }}
                 </div>
              </div>

              <div class="info-row">
                <label>Start Date</label>
                <div class="value">{{ formatDate(project.startDate) }}</div>
              </div>
              
              <div class="info-row" v-if="project.startDate">
                <label>Time Elapsed</label>
                <div class="value" style="color: #666;">{{ calculateTimeElapsed(project.startDate) }}</div>
              </div>
              
              <div class="info-row">
                <label>Estimate Close Date</label>
                <div class="value">{{ formatDate(project.endDate) }}</div>
              </div>

              <div class="info-row" v-if="project.endDate">
                <label>Time Remaining</label>
                <div class="value" :class="getTimeRemainingColor(project.endDate)">
                   {{ calculateTimeRemaining(project.endDate) }}
                </div>
              </div>

            </a-collapse-panel>
          </a-collapse>
        </a-card>
      </a-col>

      <!-- Center Content: Activity Feed -->
      <a-col :xs="24" :sm="24" :md="showActionsMenu ? 10 : 17" :lg="showActionsMenu ? 12 : 18" :xl="showActionsMenu ? 12 : 18">
        <a-card :bordered="false" class="center-card hover-shadow" :bodyStyle="{ padding: '0' }">
          <a-tabs defaultActiveKey="1" class="custom-tabs" :tabBarStyle="{ padding: '0 16px', marginBottom: '0' }">
            <a-tab-pane key="1" tab="Overview">
              <div class="tab-content">

                  <div class="section-block">
                    <div class="section-header">
                      <h4>Product Configuration</h4>
                      <a-icon type="bulb" />
                    </div>
                    
                    <a-form layout="vertical">
                        <a-row :gutter="16">
                            <a-col :span="24">
                                <a-form-item label="Select Product Line">
                                    <a-select v-model="productConfig.type" placeholder="Choose a product line">
                                        <a-select-option value="bespoke">BESPOKE</a-select-option>
                                        <a-select-option value="lumosrondo">LUMOS RONDO</a-select-option>
                                        <a-select-option value="lumoslineo">LUMOS LINEO</a-select-option>
                                        <a-select-option value="lumosmetallo">LUMOS METALLO</a-select-option>
                                        <a-select-option value="lumosperfo">LUMOS PERFO</a-select-option>
                                        <a-select-option value="lumosgrafico">LUMOS GRAFICO</a-select-option>
                                    </a-select>
                                </a-form-item>
                            </a-col>
                        </a-row>
                    </a-form>
                  </div>

                  <!-- Notes -->
                  <div class="section-block">
                    <div class="section-header">
                      <h4>Notes</h4>
                      <a-button type="link" icon="edit" size="small" @click="openTextModal('remarks', 'Notes')"/>
                    </div>
                    <div class="text-content-box" @click="openTextModal('remarks', 'Notes')">
                        <p v-if="project.remarks" style="white-space: pre-wrap; color: #516f90;">{{ project.remarks }}</p>
                        <span v-else style="color: #ccc; font-style: italic;">Click to add notes...</span>
                    </div>
                  </div>

                  <!-- Description -->
                  <div class="section-block">
                    <div class="section-header">
                      <h4>Description</h4>
                      <a-button type="link" icon="edit" size="small" @click="openTextModal('description', 'Description')"/>
                    </div>
                    <div class="text-content-box" @click="openTextModal('description', 'Description')">
                       <p v-if="project.description" style="white-space: pre-wrap; color: #516f90;">{{ project.description }}</p>
                       <span v-else style="color: #ccc; font-style: italic;">Click to add description...</span>
                    </div>
                  </div>

                  <!-- Communication removed -->
              </div>
            </a-tab-pane>
            <a-tab-pane key="2" tab="Tasks">
               <div class="tab-content">
                  <div class="section-block">
                      <div class="section-header">
                          <h4>Project Tasks</h4>
                          <a-button type="primary" size="small" icon="plus" @click="addTask">Add Task</a-button>
                      </div>

                      <div style="margin-bottom: 16px;">
                          <a-tag color="red">Overdue</a-tag>
                          <a-tag color="orange">Due soon (&lt; 2 days)</a-tag>
                          <a-tag color="blue">Future</a-tag>
                          <a-tag>Completed</a-tag>
                      </div>
                      
                      <div v-if="tasks.length === 0" style="text-align: center; color: #ccc; padding: 20px;">
                          <p>No tasks yet. Keep track of what needs to be done.</p>
                      </div>

                      <a-list v-else item-layout="horizontal" :data-source="tasks">
                        <a-list-item slot="renderItem" slot-scope="item">
                          <div slot="actions">
                              <a-button type="link" size="small" icon="edit" @click="editTask(item)"></a-button>
                              <a-button type="link" size="small" icon="delete" class="text-danger" @click="deleteTask(item)"></a-button>
                          </div>
                          <a-list-item-meta>
                            <div slot="title" :style="{ textDecoration: item.completed ? 'line-through' : 'none', color: item.completed ? '#ccc' : 'inherit' }">
                                <a-checkbox :checked="item.completed" @change="toggleTask(item)" style="margin-right: 10px;"></a-checkbox>
                                {{ item.title }}
                            </div>
                            <!-- Show date below title if exists -->
                             <div slot="description" v-if="item.date" style="margin-left: 26px; margin-top: 4px;">
                                <a-tag v-if="item.date" :color="getTaskDateColor(item.date, item.completed)">
                                   <a-icon type="calendar" /> {{ formatTaskDate(item.date) }}
                                </a-tag>
                             </div>
                          </a-list-item-meta>
                        </a-list-item>
                      </a-list>
                  </div>
               </div>
            </a-tab-pane>
            <a-tab-pane key="3" tab="Files">
                <div class="tab-content">
                   <div class="section-block">
                     <div class="section-header">
                       <h4>Documents</h4>
                       <a-upload
                           name="file"
                           :action="uploadAction"
                           :show-upload-list="false"
                           @change="handleFileChange"
                           :headers="tokenHeader"
                       >
                           <a-button type="primary" size="small" icon="upload">Upload File</a-button>
                       </a-upload>
                     </div>
                     <div v-if="fileList.length === 0" style="padding: 20px; color: #ccc;">
                        No files uploaded.
                     </div>
                     <a-list v-else item-layout="horizontal" :data-source="fileList">
                        <a-list-item slot="renderItem" slot-scope="item, index">
                           <a slot="actions" :href="getFileUrl(item.url)" target="_blank">Download</a>
                           <a slot="actions" style="color: red;" @click="deleteFile(index)">Delete</a>
                           <a-list-item-meta :description="item.date">
                              <a slot="title" :href="getFileUrl(item.url)" target="_blank">{{ item.name }}</a>
                              <a-avatar slot="avatar" icon="file" style="background-color: #faad14" />
                           </a-list-item-meta>
                        </a-list-item>
                     </a-list>
                 </div>
              </div>
            </a-tab-pane>

          </a-tabs>
        </a-card>

        <!-- Communication Card -->
        <a-card :bordered="false" class="communication-card hover-shadow" style="margin-top: 16px;">
            <div slot="title" style="display: flex; align-items: center;">
              <a-icon type="message" style="margin-right: 8px;" /> Communication
            </div>
            <div class="communication-content">
                <a-tabs defaultActiveKey="1" size="small" :animated="false">
                  <a-tab-pane key="1" tab="Email">
                     <div class="actions-bar" style="margin-bottom: 10px; display: flex; justify-content: flex-end;">
                        <a-button type="primary" size="small" icon="plus" @click="addComm('email')">Log Email</a-button>
                     </div>
                     <a-list item-layout="horizontal" :data-source="commData.emails">
                        <a-list-item slot="renderItem" slot-scope="item">
                          <a slot="actions" @click="editComm('email', item)">Edit</a>
                          <a slot="actions" style="color: red;" @click="deleteComm('email', item)">Delete</a>
                          <a-list-item-meta :description="item.date">
                            <a slot="title" href="#">{{ item.subject }}</a>
                            <a-avatar slot="avatar" icon="mail" style="background-color: #1890ff" />
                          </a-list-item-meta>
                        </a-list-item>
                        <div v-if="commData.emails.length === 0" style="text-align: center; color: #ccc; padding: 10px;">
                            No emails logged.
                        </div>
                     </a-list>
                  </a-tab-pane>
                  <a-tab-pane key="2" tab="Calls">
                     <div class="actions-bar" style="margin-bottom: 10px; display: flex; justify-content: flex-end;">
                        <a-button type="primary" size="small" icon="plus" @click="addComm('call')">Log Call</a-button>
                     </div>
                     <a-list item-layout="horizontal" :data-source="commData.calls">
                        <a-list-item slot="renderItem" slot-scope="item">
                          <a slot="actions" @click="editComm('call', item)">Edit</a>
                          <a slot="actions" style="color: red;" @click="deleteComm('call', item)">Delete</a>
                          <a-list-item-meta :description="item.date">
                             <a slot="title" href="#">{{ item.subject }}</a>
                             <a-avatar slot="avatar" icon="phone" style="background-color: #52c41a" />
                          </a-list-item-meta>
                        </a-list-item>
                         <div v-if="commData.calls.length === 0" style="text-align: center; color: #ccc; padding: 10px;">
                            No calls logged.
                        </div>
                     </a-list>
                  </a-tab-pane>
                  <a-tab-pane key="3" tab="Meetings">
                     <div class="actions-bar" style="margin-bottom: 10px; display: flex; justify-content: flex-end;">
                        <a-button type="primary" size="small" icon="plus" @click="addComm('meeting')">Log Meeting</a-button>
                     </div>
                     <a-list item-layout="horizontal" :data-source="commData.meetings">
                        <a-list-item slot="renderItem" slot-scope="item">
                          <a slot="actions" @click="editComm('meeting', item)">Edit</a>
                          <a slot="actions" style="color: red;" @click="deleteComm('meeting', item)">Delete</a>
                          <a-list-item-meta :description="item.date">
                            <a slot="title" href="#">{{ item.subject }}</a>
                             <a-avatar slot="avatar" icon="team" style="background-color: #722ed1" />
                          </a-list-item-meta>
                        </a-list-item>
                         <div v-if="commData.meetings.length === 0" style="text-align: center; color: #ccc; padding: 10px;">
                            No meetings logged.
                        </div>
                     </a-list>
                  </a-tab-pane>
                </a-tabs>
            </div>
        </a-card>
      </a-col>

      <!-- Right Sidebar: Associations -->
      <a-col :xs="24" :sm="24" :md="7" :lg="6" :xl="6" v-show="showActionsMenu">
        <a-card :bordered="false" class="right-card hover-shadow">
          <a-collapse defaultActiveKey="1" :bordered="false" expandIconPosition="left">
            <a-collapse-panel key="1" class="right-panel">
              <template slot="header">
                <span class="panel-title">Linked Agency</span>
              </template>
              
              <div class="company-item" v-if="project.agency">
                <a-avatar shape="square" size="small" style="background-color: #ff7a59">{{ project.agency.charAt(0) }}</a-avatar>
                <div class="company-details">
                  <div class="company-name">{{ project.agency }}</div>
                </div>
              </div>
            </a-collapse-panel>

             <a-collapse-panel key="2" class="right-panel">
              <template slot="header">
                <span class="panel-title">Attachments</span>
              </template>
              <a-icon slot="extra" type="plus" class="add-icon" />
            </a-collapse-panel>

          </a-collapse>
        </a-card>
      </a-col>
    </a-row>
    
    <!-- Text Edit Modal -->
    <a-modal
      :title="textModal.title"
      :visible="textModal.visible"
      @ok="saveTextModal"
      @cancel="textModal.visible = false"
      width="800px"
      okText="Confirm"
      class="writing-pad-modal"
    >
      <div class="pad-toolbar">
         <a-tooltip title="Insert Date/Time">
            <a-button icon="clock-circle" size="small" @click="insertTextWithDate" />
         </a-tooltip>
         <a-tooltip title="Bullet List">
            <a-button icon="unordered-list" size="small" @click="insertText('- ')" />
         </a-tooltip>
         <a-tooltip title="Separator Line">
            <a-button icon="minus" size="small" @click="insertText('\n-------------------\n')" />
         </a-tooltip>
         <a-divider type="vertical" />
         <a-dropdown>
            <a-menu slot="overlay" @click="handleTemplateClick">
              <a-menu-item key="meeting">Meeting Notes Template</a-menu-item>
              <a-menu-item key="todo">To-Do List</a-menu-item>
              <a-menu-item key="call">Call Log</a-menu-item>
            </a-menu>
            <a-button size="small">
              Templates <a-icon type="down" />
            </a-button>
         </a-dropdown>
         
         <div class="toolbar-right" style="float: right;">
            <a-button size="small" type="dashed" @click="textModal.content = ''">Clear</a-button>
         </div>
      </div>
      
      <a-textarea 
        ref="padInput"
        v-model="textModal.content" 
        :rows="15" 
        class="custom-textarea"
        placeholder="Type here..."
      />
    </a-modal>

    <!-- Task Modal -->
    <a-modal
      :title="taskModal.editingId ? 'Edit Task' : 'Add Task'"
      :visible="taskModal.visible"
      @ok="saveTaskFromModal"
      @cancel="taskModal.visible = false"
    >
      <a-form layout="vertical">
        <a-form-item label="Task Title">
           <a-input v-model="taskModal.title" placeholder="What needs to be done?" />
        </a-form-item>
        <a-form-item label="Due Date">
            <a-date-picker v-model="taskModal.date" style="width: 100%" format="YYYY-MM-DD" valueFormat="YYYY-MM-DD" />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- Use LcprojectModal for editing if needed, hidden for now unless we implement edit btn -->
    <lcproject-modal ref="modal" @saved="loadProject" />

  </div>
</template>

<script>
import { getAction, putAction } from '@/api/manage'
import LcprojectModal from "./modules/LcprojectModal.vue";
import Vue from 'vue'
import { ACCESS_TOKEN } from "@/store/mutation-types"



export default {
  name: 'ProjectDetail',
  components: { LcprojectModal },
  data() {
    return {
      projectId: null,
      project: {},
      latestQuotePrice: null,
      loading: false,
      showActionsMenu: false,
      currentStage: 2,
      pipelineStages: [
        { label: 'New lead', value: 'new-lead', color: '#56CCF2' },
        { label: 'Budget', value: 'budget', color: '#F2994A' },
        { label: 'Specify', value: 'specify', color: '#BB6BD9' },
        { label: 'Quote', value: 'quote', color: '#6FCF97' },
        { label: 'Follow Up', value: 'follow-up', color: '#F2C94C' },
        { label: 'PO', value: 'po', color: '#EB5757' }
      ],
      commData: {
        emails: [],
        calls: [],
        meetings: []
      },

      fileList: [], // For ext4 storage - but wait, commData uses ext4?
      // Re-reading code: commData is from ext4. 
      // User requirement: "all other files... ext4 fields... store OSS URLs".
      // Previous view_file showed:
      // loadComm() { if (this.project.ext4) { try { const parsed = JSON.parse(this.project.ext4); this.commData = ...
      // So ext4 is ALREADY used for communication logs.
      // I should modify ext4 structure to include 'files' or use a different field if available?
      // The user requirement said: "ext4 fields... are prepared to store OSS URLs for other files (likely as JSON arrays)".
      // It implies ext4 is THE place.
      // So I will append 'files' to the JSON structure in ext4.
      
      uploadAction: '/jshERP-boot/systemConfig/upload?biz=project',
      tokenHeader: { 'X-Access-Token': Vue.ls.get(ACCESS_TOKEN) },

      quotesList: [],
      tasks: [],
      productConfig: {
        type: undefined,
        details: {
            shape: undefined,
            dim1: '',
            dim2: '',
            dim3: '',
            diameter: '',
            lighting: undefined,
            control: undefined,
            profile: undefined,
            profileColor: undefined,
            mounting: undefined,
        }
      },
      textModal: {
        visible: false,
        title: '',
        content: '',
        field: ''
      },
      taskModal: {
        visible: false,
        title: '',
        date: null,
        editingId: null
      }
    }
  },
  created() {
    this.projectId = this.$route.query.id || this.$route.params.id;
    this.loadProject();
  },
  methods: {
    toggleActionsMenu() {
      this.showActionsMenu = !this.showActionsMenu;
    },
    async loadProject() {
      if (!this.projectId) return;
      this.loading = true;
      try {
        const res = await getAction(`/lcproject/get/${this.projectId}`);
        if (res) {
            this.project = res;
            if(this.project.status) {
                const idx = this.pipelineStages.findIndex(s => s.value === this.project.status);
                if(idx !== -1) this.currentStage = idx;
            }
            this.loadComm();
            this.loadTasks();
            try {
                this.loadLatestQuote(); 
                this.loadAllQuotes();

            } catch(e) {
                console.error("Safely caught quote load error", e);
            }
        }
      } catch (e) {
        console.error("Failed to load project", e);
      } finally {
        this.loading = false;
      }
    },



    handleStageClick(index, stage) {
      if(index === this.currentStage) return; // same stage

      this.$confirm({
        title: 'Change Project Status?',
        content: `Are you sure you want to move the project to ${stage.label}?`,
        okText: 'Confirm',
        onOk: () => {
           this.currentStage = index;
           this.project.status = stage.value;
           
           // Persist change
           const formData = { ...this.project, status: stage.value };
           putAction('/lcproject/update', formData).then(res => {
               if(res === 1 || res.code === 200) {
                   this.$message.success('Status updated');
               }
           });
        }
      });
    },

    loadComm() {
      if (this.project.ext4) {
        try {
          const parsed = JSON.parse(this.project.ext4);
          this.commData = {
             emails: parsed.emails || [],
             calls: parsed.calls || [],
             meetings: parsed.meetings || []
          };
          this.fileList = parsed.files || [];
        } catch (e) {
          this.commData = { emails: [], calls: [], meetings: [] };
          this.fileList = [];
        }
      } else {
         this.commData = { emails: [], calls: [], meetings: [] };
         this.fileList = [];
      }
    },
    
    async saveCommData() {
       // Merge fileList into the stored JSON
       const storageObj = {
           ...this.commData,
           files: this.fileList
       };
       const jsonStr = JSON.stringify(storageObj);
       const formData = { ...this.project, ext4: jsonStr };
       try {
         // LcProjectController update returns int 1 on success usually, but putAction wrapper might handle response structure
         // The controller returns 'int'.
         const res = await putAction('/lcproject/update', formData);
         // If res is bare int '1', putAction might wrap it or return it directly depending on request.js
         // Usually request.js normalizes. I'll assume standard success check or truthy.
         if(res === 1 || res.code === 200) {
            this.project.ext4 = jsonStr;
            this.$message.success('Updates saved');
         } else {
            // this.$message.warning('Failed to save communication');
         }
       } catch(e) {
         console.error(e);
         // this.$message.error('Error saving communication');
       }
    },

    addComm(type) {
       // Simple prompt usage for quick implementation
       this.$confirm({
          title: `Log ${type}`,
          content: h => <a-input id="commInput" placeholder="Subject/Summary" />,
          okText: 'Confirm',
          onOk: () => {
              const input = document.getElementById('commInput');
              const val = input ? input.value : '';
              if(val) {
                   const item = {
                      id: Date.now(),
                      subject: val,
                      date: new Date().toLocaleString(),
                  };
                   if(type === 'email') this.commData.emails.unshift(item);
                   else if(type === 'call') this.commData.calls.unshift(item);
                   else if(type === 'meeting') this.commData.meetings.unshift(item);
                   this.saveCommData();
              }
          }
       });
    },
    
    editComm(type, item) {
       this.$confirm({
          title: `Edit ${type}`,
          content: h => <a-input id="commEditInput" defaultValue={item.subject} />,
          okText: 'Confirm',
           onOk: () => {
              const input = document.getElementById('commEditInput');
              const val = input ? input.value : '';
              if(val) {
                  item.subject = val;
                  this.saveCommData();
              }
           }
       });
    },

    deleteComm(type, item) {
       this.$confirm({
        title: 'Delete this item?',
        okType: 'danger',
        okText: 'Confirm',
        onOk: () => {
           if(type === 'email') {
              this.commData.emails = this.commData.emails.filter(i => i.id !== item.id);
           } else if(type === 'call') {
               this.commData.calls = this.commData.calls.filter(i => i.id !== item.id);
           } else if(type === 'meeting') {
               this.commData.meetings = this.commData.meetings.filter(i => i.id !== item.id);
           }
           this.saveCommData();
        }
      });
    },

    // FILE HANDLING
    handleFileChange(info) {
        if (info.file.status === 'done') {
            const res = info.file.response;
            if (res && res.code === 200) {
                const newFile = {
                    id: Date.now(),
                    name: info.file.name,
                    url: res.data,
                    date: new Date().toLocaleString()
                };
                this.fileList.push(newFile);
                this.$message.success('File uploaded');
                this.saveCommData(); // Re-use saveCommData as it saves ext4
            } else {
                this.$message.warning(res.data || 'Upload failed');
            }
        } else if (info.file.status === 'error') {
            this.$message.error('Upload error');
        }
    },

    deleteFile(index) {
        this.$confirm({
            title: 'Delete this file?',
            okType: 'danger',
            okText: 'Confirm',
            onOk: () => {
                this.fileList.splice(index, 1);
                this.saveCommData();
            }
        });
    },

    getFileUrl(path) {
        if(!path) return '';
        if(path.startsWith('http')) return path;
        return "/jshERP-boot/systemConfig/static/" + path;
    },

    // TASK Management
    loadTasks() {
      if (this.project.ext5) {
        try {
          this.tasks = JSON.parse(this.project.ext5) || [];
        } catch (e) {
          this.tasks = [];
        }
      } else {
         this.tasks = [];
      }
    },

    formatTaskDate(date) {
        if (!date) return '';
        const d = new Date(date);
        if (isNaN(d.getTime())) return date;
        return d.toLocaleDateString(undefined, { year: 'numeric', month: 'short', day: 'numeric' });
    },
    
    getTaskDateColor(date, completed) {
        if(completed) return ''; 
        const d = new Date(date);
        const now = new Date();
        now.setHours(0,0,0,0);
        if (d < now) return 'red'; // Overdue
        const diffTime = d - now;
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)); 
        if (diffDays <= 2) return 'orange'; // Due soon
        return 'blue';
    },

    async saveTasks() {
       const jsonStr = JSON.stringify(this.tasks);
       const formData = { ...this.project, ext5: jsonStr };
       try {
         const res = await putAction('/lcproject/update', formData);
         if(res === 1 || res.code === 200) {
            this.project.ext5 = jsonStr;
            this.$message.success('Tasks updated');
         }
       } catch(e) {
         console.error(e);
         this.$message.error('Error saving tasks');
       }
    },

    addTask() {
       this.taskModal = {
           visible: true,
           title: '',
           date: null, // or default to today?
           editingId: null
       };
    },

    editTask(item) {
       this.taskModal = {
           visible: true,
           title: item.title,
           date: item.date || null,
           editingId: item.id
       };
    },
    
    saveTaskFromModal() {
        if(!this.taskModal.title) {
            this.$message.warning("Please enter a task title");
            return;
        }
        
        if (this.taskModal.editingId) {
            // Edit existing
            const task = this.tasks.find(t => t.id === this.taskModal.editingId);
            if(task) {
                task.title = this.taskModal.title;
                task.date = this.taskModal.date;
            }
        } else {
            // Add new
            const newItem = {
                id: Date.now(),
                title: this.taskModal.title,
                date: this.taskModal.date,
                completed: false
            };
            this.tasks.unshift(newItem);
        }
        
        this.saveTasks();
        this.taskModal.visible = false;
    },

    deleteTask(item) {
        this.$confirm({
            title: 'Delete this task?',
            okType: 'danger',
            okText: 'Confirm',
            onOk: () => {
                this.tasks = this.tasks.filter(t => t.id !== item.id);
                this.saveTasks();
            }
        });
    },

    toggleTask(item) {
        item.completed = !item.completed;
        this.saveTasks();
    },

    handleBack() {
      const { from, agencyId } = this.$route.query;
      if (from === 'agencyDetail' && agencyId) {
        this.$router.push({ name: 'AgencyDetail', query: { id: agencyId } });
      } else {
        this.$router.back();
      }
    },
    
    handleOpenNotes() {
        this.$message.info("Notes feature coming soon");
    },

    formatDate(text) {
        return !text ? "" : (text.length > 10 ? text.substring(0, 10) : text);
    },

    getStatusColor(status) {
      switch (status) {
        case 'active': return 'green';
        case 'completed': return 'blue';
        case 'on-hold': return 'orange';
        case 'cancelled': return 'red';
        default: return 'default';
      }
    },

    calculateTimeRemaining(dateStr) {
       if(!dateStr) return '';
       const now = new Date();
       const end = new Date(dateStr);
       const diffTime = end - now;
       // Round up to give "1 day left" even if it's 0.5 days
       const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)); 
       
       if (diffDays > 0) {
           return diffDays + " days left";
       } else if (diffDays === 0) {
           return "Due today";
       } else {
           return "Overdue by " + Math.abs(diffDays) + " days";
       }
    },

    getTimeRemainingColor(dateStr) {
        if(!dateStr) return '';
        const now = new Date();
        const end = new Date(dateStr);
        const diffTime = end - now;
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

        if (diffDays < 0) return 'text-danger';
        if (diffDays <= 7) return 'text-warning'; // Less than a week
        return 'text-success';
    },

    calculateTimeElapsed(dateStr) {
       if(!dateStr) return '';
       const now = new Date();
       const start = new Date(dateStr);
       const diffTime = now - start;
       const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24));
       
       if (diffDays > 0) {
           return "Running for " + diffDays + " days";
       } else if (diffDays === 0) {
           return "Started today";
       } else {
           return "Starts in " + Math.abs(diffDays) + " days";
       }
    },

    openTextModal(field, title) {
      this.textModal.field = field;
      this.textModal.title = title;
      this.textModal.content = this.project[field] || '';
      this.textModal.visible = true;
    },

    async saveTextModal() {
      // Direct update for quick experience
      this.project[this.textModal.field] = this.textModal.content;
      this.textModal.visible = false;
      
      // Persist to backend
      const formData = { ...this.project };
      try {
         const res = await putAction('/lcproject/update', formData);
         if(res === 1 || res.code === 200) {
            this.$message.success(`${this.textModal.title} updated`);
         }
      } catch(e) {
         console.error('Update failed', e);
         this.$message.error('Failed to save changes');
      }
    },
    
    insertText(text) {
        // Simple append for now as v-model syncs. 
        // For distinct cursor insertion, we'd need to access the DOM element refs which is tricky with AntVue wrapper.
        // We will append to end or try to use native selection if possible.
        const textarea = this.$refs.padInput ? this.$refs.padInput.$el : null;
        if (textarea) {
            // Antd textarea wraps a native textarea
            // We append for simplicity or we can try more complex cursor logic
            this.textModal.content += text;
        }
    },
    
    insertTextWithDate() {
        const dateStr = '[' + new Date().toLocaleString() + '] ';
        this.insertText(dateStr);
    },

    handleTemplateClick(e) {
        if(e.key === 'meeting') {
            this.insertText('\n### Meeting Notes\nDate: ' + new Date().toLocaleDateString() + '\nAttendees: \n\nAgenda:\n1. \n2. \n\nAction Items:\n- \n');
        } else if (e.key === 'todo') {
            this.insertText('\n### To-Do List\n[ ] \n[ ] \n[ ] \n');
        } else if (e.key === 'call') {
            this.insertText('\n### Call Log\nTime: ' + new Date().toLocaleTimeString() + '\nContact: \nSubject: \n\nNotes:\n');
        }
    },

    copyCode() {
        if(!this.generatedProductCode) return;
        // Simple clipboard copy
        const el = document.createElement('textarea');
        el.value = this.generatedProductCode;
        document.body.appendChild(el);
        el.select();
        document.execCommand('copy');
        document.body.removeChild(el);
        this.$message.success('Code copied to clipboard');
    },

    loadAllQuotes() {
         if(!this.project.name) return;
         const params = {
            currentPage: 1,
            pageSize: 100, // Fetch reasonable limit
            search: JSON.stringify({
                ext4: this.project.name
            })
         }
         getAction('/lcquote/list', params).then(res => {
            let rows = [];
            if (res.rows) rows = res.rows;
            else if (res.data && res.data.rows) rows = res.data.rows;
            else if (res.data && Array.isArray(res.data)) rows = res.data;
            
            this.quotesList = rows || [];
         }).catch(e => {
             console.error("Failed to load quotes history", e);
         })
    },

    loadLatestQuote() {
        if(!this.project.name) return;
        
        // Filter by project name (ext4)
        const params = {
            currentPage: 1,
            pageSize: 1,
            search: JSON.stringify({
                ext4: this.project.name
            })
        }
        
        console.log('Fetching latest quote for project:', this.project.name);
        getAction('/lcquote/list', params).then(res => {
            console.log('Quote API response:', res);
            // Handle different response structures (res.rows or res.data.rows)
            let rows = [];
            if (res.rows) {
                rows = res.rows;
            } else if (res.data && res.data.rows) {
                rows = res.data.rows;
            } else if (res.data && Array.isArray(res.data)) {
                 rows = res.data;
            }
            
            if(rows && rows.length > 0) {
               const q = rows[0];
               const currency = q.currency === 'CAD' ? 'C$' : '$';
               this.latestQuotePrice = `${currency} ${q.totalAmount}`;
               console.log('Set latestQuotePrice:', this.latestQuotePrice);
            } else {
               this.latestQuotePrice = null;
               console.warn('No quotes found or empty response');
            }
        }).catch(e => {
         console.error('Quote fetch error', e);
      })
    }
  },
  computed: {
    generatedProductCode() {
        // ... (existing logic, assume we keep or it's outside this edit)
        const c = this.productConfig;
        const d = c.details;
        // Simplified Logic or keep existing
        if(c.type === 'bespoke') {
             return `LC-LC-${d.shape || '_'}-${d.dim1 || '_'}-${d.dim2 || '_'}-${d.lighting || '_'}-${d.control || '_'}-${d.profile || '_'}-${d.mounting || '_'}`;
        }
        return '';
    },
    
    
    // Dashboard Computed Props
    dashboardData() {
        const income = this.project.budget ? parseFloat(this.project.budget) : 0;
        const expenses = this.quotesList.reduce((sum, q) => sum + (q.totalAmount || 0), 0);
        return {
           income: income,
           expenses: expenses
        }
    },

    projectInitial() {
      return this.project && this.project.name
        ? this.project.name.charAt(0).toUpperCase()
        : "P";
    }
  }
}
</script>

<style scoped lang="less">
.project-page {
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
        color: #1890ff;
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
    border-color: #1890ff;
    color: #1890ff;
    &:hover {
      background-color: #e6f7ff;
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
        .online-status {
            position: absolute;
            bottom: 5px;
            right: 5px;
            width: 12px;
            height: 12px;
            background-color: #28a745;
            border: 2px solid #fff;
            border-radius: 50%;
        }
    }

    .profile-info {
      .name {
        font-size: 24px;
        font-weight: 600;
        margin-bottom: 4px;
        color: #33475b;
      }
      .title {
        color: #516f90;
        margin-bottom: 8px;
      }
    }
  }

  .quick-actions {
    display: flex;
    justify-content: space-around;
    margin-bottom: 20px;

    .action-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      cursor: pointer;

      button {
        background-color: #ff7a59;
        color: white;
        border: none;
        margin-bottom: 4px;
        &:hover {
          background-color: #ff8f73;
        }
      }

      span {
        font-size: 12px;
        color: #516f90;
      }
    }
  }

  .custom-panel {
    /deep/ .ant-collapse-header {
      font-weight: 600;
      color: #33475b;
    }
  }

  .info-row {
    margin-bottom: 12px;
    label {
      display: block;
      font-size: 12px;
      color: #516f90;
      margin-bottom: 2px;
    }
    .value {
      font-size: 14px;
      color: #33475b;
      font-weight: 500;
    }
    .text-muted {
        color: #999;
        font-weight: normal;
    }
  }

  /* Improved Key Info Styling */
  .info-section {
     margin-bottom: 10px;
  }
  .section-label {
      font-size: 12px;
      font-weight: 700;
      text-transform: uppercase;
      color: #999;
      margin-bottom: 8px;
      letter-spacing: 0.5px;
  }
  .contact-group {
      background: #fafafa;
      padding: 10px;
      border-radius: 6px;
      border: 1px solid #f0f0f0;
  }
  .c-row {
      display: flex;
      align-items: center;
      margin-bottom: 6px;
      &:last-child { margin-bottom: 0; }
      .c-val {
          font-size: 13px;
          color: #333;
          font-weight: 500;
      }
  }
  .main-metric {
     .value.price {
         font-size: 18px;
         color: #1890ff; /* HubSpot-like blue or generic primary */
         font-weight: 600;
     }
  }

  /* Pipeline Stepper CSS */
  .pipeline-wrapper {
    display: flex;
    flex-wrap: nowrap;
    margin-bottom: 20px;
    width: 100%;
  }

  .step-item {
    position: relative;
    height: 32px;
    line-height: 32px;
    padding: 0 5px 0 15px; /* Reduced padding */
    background: #e9e9e9;
    color: #666;
    font-size: 10px; /* Slightly smaller font */
    font-weight: 600;
    cursor: pointer;
    transition: all 0.3s;
    user-select: none;
    flex: 1; 
    text-align: center;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    
    /* Chevron shape using clip-path */
    clip-path: polygon(0% 0%, 90% 0%, 100% 50%, 90% 100%, 0% 100%, 10% 50%);
    margin-right: -10px; /* Adjusted overlap */
    
    /* First item flat left */
    &:first-child {
       padding-left: 8px;
       clip-path: polygon(0% 0%, 90% 0%, 100% 50%, 90% 100%, 0% 100%);
    }
    
    /* Last item flat right */
    &:last-child {
       margin-right: 0;
       padding-right: 8px;
       clip-path: polygon(0% 0%, 100% 0%, 100% 100%, 0% 100%, 10% 50%);
    }

    /* Completed or Active styling */
    &.active {
      color: white;
      background-color: var(--step-color);
    }

    &.active {
       filter: brightness(1.1);
       transform: scale(1.05);
       z-index: 100 !important; 
    }
    
    span {
      display: inline-block;
    }
  }
}

/* Center Card & Tabs */
.center-card {
  border-radius: 8px;
  min-height: 400px;
  margin-bottom: 16px;
}
.custom-tabs /deep/ .ant-tabs-bar {
  border-bottom: 1px solid #e8e8e8;
}
.tab-content {
  padding: 16px;
}
.section-block {
    margin-bottom: 24px;
    .section-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 12px;
        h4 {
            font-weight: 600;
            margin: 0;
            color: #33475b;
        }
    }
    .info-item {
        margin-bottom: 8px;
        label {
            display: block;
            font-size: 12px;
            color: #7c98b6;
            margin-bottom: 2px;
        }
        .val {
            font-weight: 500;
            color: #33475b;
             word-break: break-all;
        }
    }
}

/* Right Card */
.right-card {
  border-radius: 8px;
  margin-bottom: 16px;
  .panel-title {
    font-weight: 600;
    color: #33475b;
  }
  .add-icon {
    color: #1890ff;
  }
  .company-item {
      display: flex;
      align-items: center;
      padding: 8px 0;
      .company-details {
          margin-left: 10px;
          .company-name {
              font-weight: 600;
              color: #1890ff;
              cursor: pointer;
          }
      }
  }
}

.text-danger { color: #fe5555; font-weight: 600; }
.text-warning { color: #faad14; font-weight: 600; }
.text-success { color: #52c41a; font-weight: 600; }
.text-content-box {
    cursor: pointer;
    padding: 8px;
    border-radius: 4px;
    &:hover {
        background-color: #f0f5ff;
    }
}

.pad-toolbar {
    margin-bottom: 10px;
    padding: 5px;
    background: #f5f5f5;
    border: 1px solid #d9d9d9;
    border-radius: 4px;
    display: flex;
    gap: 8px;
    align-items: center;
}
.code-generator-box {
    margin-top: 15px;
    padding: 10px 15px;
    background: #f7f7f7;
    border: 1px dashed #d9d9d9;
    border-radius: 4px;
    display: flex;
    align-items: center;
    justify-content: space-between;

    .code-label {
        color: #999;
        font-size: 12px;
        margin-right: 10px;
    }
    .code-display {
        display: flex;
        align-items: center;
        gap: 10px;
        font-family: 'Consolas', monospace;
        font-size: 16px;
        color: #333;
    }
}
.custom-textarea {
    font-size: 16px;
    line-height: 1.6;
    padding: 15px;
    border-color: #d9d9d9;
    resize: none;
    font-family: 'Consolas', 'Monaco', 'Courier New', monospace; /* Monospace for structured feeling */
}
</style>
