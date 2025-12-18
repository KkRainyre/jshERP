<template>
  <a-modal
    :visible="visible"
    :title="isEdit ? 'Edit Project' : 'Add Project'"
    :confirmLoading="confirmLoading"
    @ok="save"
    @cancel="close"
    okText="Save"
    width="800px"
  >
    <a-form :form="form" layout="vertical">
      <a-row :gutter="24">
        
        <!-- Project Name -->
        <a-col :span="12">
          <a-form-item label="Project Name">
            <a-input 
              v-decorator="['name', {rules: [{ required: true, message: 'Please input project name!' }, { max: 100, message: 'Name cannot exceed 100 characters' }]}]" 
              placeholder="Project Name" 
            />
          </a-form-item>
        </a-col>

        <!-- Client -->
        <a-col :span="12">
          <a-form-item label="Agency">
            <a-select
              v-decorator="['agency', {rules: [{ required: true, message: 'Please select agency!' }]}]"
              placeholder="Select Agency"
              showSearch
              allowClear
              :filterOption="filterOption"
              :disabled="clientDisabled"
              @change="handleAgencyChange"
            >
              <a-select-option v-for="item in agencyList" :key="item.id" :value="item.name">
                {{ item.name }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <!-- Assigned Agents -->
        <a-col :span="24">
          <a-form-item label="Assigned Agents">
            <a-select
              mode="multiple"
              v-decorator="['assignedAgents', {rules: [{ required: true, message: 'Please select at least one agent!' }]}]"
              placeholder="Select Agents"
              style="width: 100%"
            >
              <a-select-option v-for="agent in agentList" :key="agent.id" :value="agent.id">
                {{ agent.name }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <!-- Status -->
        <a-col :span="12">
          <a-form-item label="Status">
            <a-select 
              v-decorator="['status', {rules: [{ required: true, message: 'Please select status!' }]}]" 
              placeholder="Select Status"
            >
              <a-select-option value="new-lead">
                 <a-tag color="#56CCF2">New Lead</a-tag>
              </a-select-option>
              <a-select-option value="budget">
                 <a-tag color="#F2994A">Budget</a-tag>
              </a-select-option>
              <a-select-option value="specify">
                 <a-tag color="#BB6BD9">Specify</a-tag>
              </a-select-option>
              <a-select-option value="quote">
                 <a-tag color="#6FCF97">Quote</a-tag>
              </a-select-option>
              <a-select-option value="follow-up">
                 <a-tag color="#F2C94C">Follow Up</a-tag>
              </a-select-option>
              <a-select-option value="po">
                 <a-tag color="#EB5757">PO</a-tag>
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>

        <!-- Location -->
        <a-col :span="12">
          <a-form-item label="Location">
            <a-input 
              v-decorator="['location', {rules: [{ max: 100, message: 'Location cannot exceed 100 characters' }]}]" 
              placeholder="Project Location" 
            />
          </a-form-item>
        </a-col>

        <!-- Start Date -->
        <a-col :span="12">
          <a-form-item label="Start Date">
            <a-date-picker 
              v-decorator="['startDate']" 
              style="width: 100%"
              format="YYYY-MM-DD"
              :disabled-date="disabledStartDate"
            />
          </a-form-item>
        </a-col>

        <!-- End Date -->
        <a-col :span="12">
          <a-form-item label="End Date">
            <a-date-picker 
              v-decorator="['endDate']" 
              style="width: 100%"
              format="YYYY-MM-DD"
              :disabled-date="disabledEndDate"
            />
          </a-form-item>
        </a-col>

        <!-- Budget -->
        <a-col :span="12">
          <a-form-item label="Budget">
            <a-input-number 
              v-decorator="['budget']" 
              style="width: 100%"
              :min="0"
              :precision="2"
              :formatter="value => `$ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
              :parser="value => value.replace(/\$\s?|(,*)/g, '')"
              placeholder="0.00"
            />
          </a-form-item>
        </a-col>

        <!-- Project Manager -->
        <a-col :span="12">
          <a-form-item label="Email">
            <a-input 
              v-decorator="['manager', {rules: [{ max: 50, message: 'Manager name cannot exceed 50 characters' }]}]" 
              placeholder="Project Manager Name" 
            />
          </a-form-item>
        </a-col>

        <!-- Contact Person -->
        <a-col :span="12">
          <a-form-item label="Contact Person">
            <a-input 
              v-decorator="['contactPerson', {rules: [{ max: 50, message: 'Contact person cannot exceed 50 characters' }]}]" 
              placeholder="Contact Person" 
            />
          </a-form-item>
        </a-col>

        <!-- Contact Phone -->
        <a-col :span="12">
          <a-form-item label="Contact Phone">
            <a-input 
              v-decorator="['contactPhone', {rules: [{ validator: validatePhone }]}]" 
              placeholder="Contact Phone Number" 
            />
          </a-form-item>
        </a-col>



        <!-- Remarks -->
        <a-col :span="24">
          <a-form-item label="Remarks">
            <a-textarea 
              v-decorator="['remarks', {rules: [{ max: 500, message: 'Remarks cannot exceed 500 characters' }]}]"
              placeholder="Remarks"
              :rows="4"
            />
          </a-form-item>
        </a-col>

        <!-- Description -->
        <a-col :span="24">
          <a-form-item label="Description">
            <a-textarea 
              v-decorator="['description', {rules: [{ max: 500, message: 'Description cannot exceed 500 characters' }]}]"
              placeholder="Project Description"
              :rows="4"
            />
          </a-form-item>
        </a-col>


      </a-row>
    </a-form>
  </a-modal>
</template>

<script>
import { axios as request } from "@/utils/request";
import moment from "moment";

export default {
  name: "LcprojectModal",

  data() {
    return {
      visible: false,
      confirmLoading: false,
      isEdit: false,
      clientDisabled: false,
      form: this.$form.createForm(this),
      model: {},
      agentList: [],
      agencyList: [], // Added agency list
      targetCompanyId: null, // Added for company ID
    };
  },

  methods: {
    open(record, options = {}) {
      this.visible = true;
      this.isEdit = !!record;
      this.model = record ? { ...record } : {};
      this.clientDisabled = !!options.clientDisabled;
      // Store companyId (clientId) if passed, or retrieve from record
      this.targetCompanyId = options.clientId || (record ? record.ext2 : null);

      // Attempt to resolve ID from name if missing (for legacy data)
      if (!this.targetCompanyId && record && record.agency && this.agencyList.length > 0) {
          const match = this.agencyList.find(a => a.name === record.agency);
          if (match) this.targetCompanyId = match.id;
      }
      
      // Reload agent list to filter by company if applicable
      this.loadAgentList();

      this.$nextTick(() => {
        this.form.resetFields();
        if (this.isEdit) {
          // Convert date strings to moment objects for date pickers
          const formData = { ...this.model };
          if (formData.startDate) {
            formData.startDate = moment(formData.startDate);
          }
          if (formData.endDate) {
            formData.endDate = moment(formData.endDate);
          }
          // Convert comma-separated string to array for multi-select. 
          // Note: ext1 now stores IDs. Select component handles integer/string conversion usually, but safe to ensure strings if needed.
          if (formData.ext1) {
            // Ensure we are working with types matching select (IDs)
            const ids = formData.ext1.split(',');
            // If the IDs in DB are strings, this is fine. If they are stored as string but represent numbers, Select might be strict.
            // Assuming IDs are numbers, we might need to map to numbers. But split returns strings.
            // Let's assume select values (agent.id) are NUMBERS (Long).
            formData.assignedAgents = ids.map(id => Number(id)); 
          } else {
            formData.assignedAgents = [];
          }
          this.form.setFieldsValue(formData);
        } else if (options.clientName) {
           this.form.setFieldsValue({ agency: options.clientName });
        }
      });
    },

    close() {
      this.visible = false;
      this.form.resetFields();
      this.model = {};
      this.targetCompanyId = null;
    },

    save() {
      this.form.validateFields(async (err, values) => {
        if (!err) {
          this.confirmLoading = true;

          try {
            // Convert moment objects to date strings
            const formData = { ...values };
            if (formData.startDate) {
              formData.startDate = formData.startDate.format("YYYY-MM-DD");
            }
            if (formData.endDate) {
              formData.endDate = formData.endDate.format("YYYY-MM-DD");
            }
            // Convert assignedAgents array to comma-separated string (IDs)
            if (formData.assignedAgents && formData.assignedAgents.length > 0) {
              formData.ext1 = formData.assignedAgents.join(',');
            } else {
              formData.ext1 = '';
            }
            
            // Set company_id (EXT2)
            if (this.targetCompanyId) {
               formData.ext2 = this.targetCompanyId.toString();
            }

            // Remove assignedAgents from payload as it's not a direct field
            delete formData.assignedAgents;

            const payload = this.isEdit 
              ? { ...this.model, ...formData }
              : formData;

            const url = this.isEdit ? "/lcproject/update" : "/lcproject/add";
            const method = this.isEdit ? "PUT" : "POST";

            const res = await request({
              url,
              method,
              data: payload
            });

            if (res === 1 || (res && res.code === 200)) {
              this.$message.success(
                this.isEdit ? "Project updated successfully" : "Project added successfully"
              );
              this.$emit("saved");
              this.close();
            } else {
              this.$message.error((res.data && res.data.message) || "Operation failed");
            }
          } catch (error) {
            console.error("Save failed:", error);
            this.$message.error("Failed to save project");
          } finally {
            this.confirmLoading = false;
          }
        }
      });
    },

    validatePhone(rule, value, callback) {
      if (!value) {
        callback();
      } else {
        // Allow digits, spaces, dashes, plus sign. At least 7 chars.
        if (!/^[\d\+\-\s]{7,20}$/.test(value)) {
          callback('Please enter a valid phone number');
        } else {
          callback();
        }
      }
    },

    disabledStartDate(startValue) {
      const endValue = this.form.getFieldValue('endDate');
      if (!startValue || !endValue) {
        return false;
      }
      return startValue.valueOf() > endValue.valueOf();
    },

    disabledEndDate(endValue) {
      const startValue = this.form.getFieldValue('startDate');
      if (!endValue || !startValue) {
        return false;
      }
      return endValue.valueOf() < startValue.valueOf();
    },

    async loadAgentList() {
      // Strict filtering: If no agency selected, show no agents.
      if (!this.targetCompanyId) {
          this.agentList = [];
          return;
      }

      try {
        const params = { companyId: this.targetCompanyId };
        
        const res = await request({
          url: '/lcagent/select',
          method: 'get',
          params
        });
        if (res && res.data) {
          this.agentList = res.data;
        } else {
          this.agentList = [];
        }
      } catch (e) {
        console.error("Failed to load agent list", e);
        this.agentList = [];
      }
    },

    loadAgencyList() {
      request({
        url: '/agency/list',
        method: 'get',
        params: { pageSize: 1000 }
      }).then(res => {
         if (res && res.rows) {
           this.agencyList = res.rows;
         } else if (res && res.data && res.data.rows) {
           this.agencyList = res.data.rows;
         }
      });
    },

    filterOption(input, option) {
      return (
        option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
      );
    },

    handleAgencyChange(value) {
       // Find selected agency
       const agency = this.agencyList.find(a => a.name === value);
       if (agency) {
          this.targetCompanyId = agency.id;
          // Refresh agent list for this agency
          this.loadAgentList();
          // Clear currently selected agents as they might not belong to new agency
          this.form.setFieldsValue({ assignedAgents: [] });
       } else {
          this.targetCompanyId = null;
          this.agentList = []; 
          this.form.setFieldsValue({ assignedAgents: [] });
       }
    }
  },
  mounted() {
    this.loadAgentList();
    this.loadAgencyList();
  }
};
</script>

<style scoped>
/* Add any custom styles here if needed */
</style>
