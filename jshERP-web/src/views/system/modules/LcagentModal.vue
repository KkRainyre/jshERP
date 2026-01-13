<template>
  <a-modal
    :visible="visible"
    :title="isEdit ? 'Edit Agent' : 'Add Agent'"
    :confirmLoading="confirmLoading"
    @ok="save"
    @cancel="close"
    okText="Save"
  >
    <a-form :form="form" layout="vertical">
      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="Name">
            <a-input v-decorator="['name', {rules: [{ required: true, message: 'Please input name!' }]}]" placeholder="Agent Name" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="Title">
            <a-select
              v-decorator="['title', {rules: [{ required: true, message: 'Please select title!' }]}]"
              placeholder="Select Title"
            >
              <a-select-option value="Principle">Principle</a-select-option>
              <a-select-option value="Spec-Sales">Spec-Sales</a-select-option>
              <a-select-option value="Sales">Sales</a-select-option>
              <a-select-option value="Distributor">Distributor</a-select-option>
              <a-select-option value="Quotation">Quotation</a-select-option>
              <a-select-option value="Project Manager">Project Manager</a-select-option>
              <a-select-option value="Marketing">Marketing</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        
        <a-col :span="12">
          <a-form-item label="Phone">
            <a-input v-decorator="['phone', {rules: [{ required: true, message: 'Please input phone!' }]}]" placeholder="Mobile Phone" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="Email">
            <a-input v-decorator="['email', {rules: [{ required: true, message: 'Please input email!' }, { type: 'email', message: 'Please enter a valid email!' }]}]" placeholder="Email Address" />
          </a-form-item>
        </a-col>

        <a-col :span="12">
          <a-form-item label="Office Phone">
            <a-input v-decorator="['officeNum', {rules: [{ required: true, message: 'Please input office phone!' }]}]" placeholder="Office Phone" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="Extension">
            <a-input v-decorator="['phoneExt']" placeholder="e.g. 101" />
          </a-form-item>
        </a-col>

        <a-col :span="12">
          <a-form-item label="Company">
            <a-select
              v-decorator="['companyId', {rules: [{ required: true, message: 'Please select company!' }]}]"
              placeholder="Select or Search Company"
              showSearch
              allowClear
              :filterOption="filterOption"
              :disabled="disabledCompany"
            >
              <a-select-option
                v-for="item in agencyList"
                :key="item.id"
                :value="item.id"
              >
                {{ item.name }}
              </a-select-option>
            </a-select>

          </a-form-item>
        </a-col>

        <a-col :span="12">
           <a-form-item label="Country">
             <a-input v-decorator="['country']" placeholder="Country" />
           </a-form-item>
        </a-col>



        <a-col :span="24">
          <a-form-item label="Remark">
            <a-textarea v-decorator="['remark']" :rows="3" />
          </a-form-item>
        </a-col>

        <a-col :span="24">
          <a-form-item label="Upload Logo" :labelCol="{span: 5}" :wrapperCol="{span: 19}">
         <a-upload
            name="file"
            :action="uploadAction"
            :show-upload-list="false"
            @change="handleLogoChange"
            :headers="tokenHeader"
          >
            <a-button icon="upload">Choose Logo</a-button>
          </a-upload>
          <div v-if="previewLogo" style="margin-top: 10px;">
            <img :src="previewLogo" alt="logo" style="max-width: 100px; max-height: 100px;"/>
          </div>
        </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script>
import { axios as request } from "@/utils/request";
import { getAction } from "@/api/manage";
import { addAgent, editAgent } from '@/api/api'
import { mixinDevice } from '@/utils/mixin'
import Vue from 'vue'
import { ACCESS_TOKEN } from "@/store/mutation-types"

export default {
  name: "LcagentModal",

  data() {
    return {
      visible: false,
      isEdit: false,
      form: this.$form.createForm(this),

      record: null,
      fileData: null, // This will no longer be used for direct file data, but keeping for now if other parts rely on it.
      previewLogo: null,
      confirmLoading: false,
      agencyList: [],
      disabledCompany: false,
      uploadAction: window._CONFIG['domianURL'] + '/systemConfig/upload?biz=agent',
      tokenHeader: { 'X-Access-Token': Vue.ls.get(ACCESS_TOKEN) },
      model: {} // Added model to store form data including logo path
    };
  },

  methods: {

    filterOption(input, option) {
      const text = option.componentOptions.children[0].text.toLowerCase();
      return text.indexOf(input.toLowerCase()) >= 0;
    },

    loadAgencies() {
      getAction('/agency/list', {
        pageNum: 1,
        pageSize: 1000
      }).then(res => {
         console.log("loadAgencies response:", res);
         if (res && res.data && res.data.rows) {
           this.agencyList = res.data.rows;
         } else if (res && res.rows) {
           this.agencyList = res.rows;
         } else if (res && res.result) {
            if (Array.isArray(res.result)) {
               this.agencyList = res.result;
            } else if (res.result.records) {
               this.agencyList = res.result.records;
            }
         } else if (res && Array.isArray(res)) {
             this.agencyList = res;
         }
      }).catch(err => {
         console.error("loadAgencies error:", err);
      });
    },

    open(record, defaults = {}) {
      this.visible = true;
      this.loadAgencies(); // Load list when modal opens
      this.isEdit = !!record;
      this.record = record || {};
      this.model = { ...this.record }; // Initialize model with record data

      this.form.resetFields();
      this.previewLogo = null; // Reset previewLogo on open

      this.$nextTick(() => {
        const fields = [
          'name', 'title', 'companyId',
          'phone', 'email', 'officeNum', 'phoneExt',
          'address', 'shipping', 'currency', 'category', 'remark', 'country'
        ];

        let data = {};

        fields.forEach(k => {
          if (this.model[k] !== undefined && this.model[k] !== null) { 
            data[k] = this.model[k];
          }
        });

        if (!this.isEdit && defaults) {
          data = { ...data, ...defaults };
        }

        this.form.setFieldsValue(data);

        // Set previewLogo based on model.logo
        if (this.model.logo) {
           if (this.model.logo.startsWith('http')) {
              this.previewLogo = this.model.logo;
           } else {
              this.previewLogo = "/jshERP-boot/systemConfig/static/" + this.model.logo;
           }
        } else {
           this.previewLogo = null;
        }
      });
    },

    close() {
      this.visible = false;
      this.previewLogo = null; // Clear preview on close
      this.model = {}; // Clear model on close
    },

    handleLogoChange(info) {
      if (info.file.status === 'done') {
        const res = info.file.response;
        if (res && res.code === 200) {
          this.model.logo = res.data; 
          if(res.data.startsWith('http')) {
              this.previewLogo = res.data;
          } else {
              this.previewLogo = "/jshERP-boot/systemConfig/static/" + res.data;
          }
          this.$message.success('Logo uploaded successfully');
        } else {
          this.$message.warning(res.data || 'Upload failed');
        }
      } else if (info.file.status === 'error') {
        this.$message.error('Upload error');
      }
    },

    async save() {
      this.form.validateFields(async (err, values) => {
        if (!err) {
          this.confirmLoading = true;
          try {
            let url = this.isEdit ? "/lcagent/update" : "/lcagent/add";

            const payload = { ...values };

            if (this.isEdit && this.record) {
              payload.id = this.record.id;
            }
            // Use model.logo for the payload
            if (this.model.logo) {
              payload.logo = this.model.logo;
            }

            await request({
              url,
              method: "post",
              data: payload
            });

            this.$message.success("Saved!");
            this.visible = false;
            this.$emit("saved");
          } catch (e) {
            console.error(e);
          } finally {
            this.confirmLoading = false;
          }
        }
      });
    }
  }
};
</script>


<style>
.logo-preview {
  width: 120px;
  margin-top: 8px;
  border: 1px solid #ccc;
  padding: 4px;
}
</style>
