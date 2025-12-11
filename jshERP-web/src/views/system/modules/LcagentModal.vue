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
            <a-input v-decorator="['title', {rules: [{ required: true, message: 'Please input title!' }]}]" placeholder="Job Title" />
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
          <a-form-item label="Category">
            <a-input v-decorator="['category', {rules: [{ required: true, message: 'Please input category!' }]}]" />
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
           <a-form-item label="Shipping">
             <a-input v-decorator="['shipping', {rules: [{ required: true, message: 'Please input shipping info!' }]}]" placeholder="Shipping Info" />
           </a-form-item>
        </a-col>
        <a-col :span="12">
           <a-form-item label="Currency">
             <a-input v-decorator="['currency', {rules: [{ required: true, message: 'Please input currency!' }]}]" />
           </a-form-item>
        </a-col>
        <a-col :span="12">
           <a-form-item label="Country">
             <a-input v-decorator="['country']" placeholder="Country" />
           </a-form-item>
        </a-col>

        <a-col :span="24">
          <a-form-item label="Address">
            <a-input v-decorator="['address', {rules: [{ required: true, message: 'Please input address!' }]}]" placeholder="Street Address" />
          </a-form-item>
        </a-col>

        <a-col :span="24">
          <a-form-item label="Remark">
            <a-textarea v-decorator="['remark']" :rows="3" />
          </a-form-item>
        </a-col>

        <a-col :span="24">
          <a-form-item label="Logo">
            <input type="file" @change="handleFile" />
            <div style="margin-top:8px;" v-if="previewLogo">
              <img :src="previewLogo" class="logo-preview" style="max-height: 80px; object-fit: contain; border: 1px dashed #ddd; padding: 4px;" />
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

export default {
  name: "LcagentModal",

  data() {
    return {
      visible: false,
      isEdit: false,
      form: this.$form.createForm(this),

      record: null,
      fileData: null,
      previewLogo: null,
      confirmLoading: false,
      agencyList: []
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
           // this.$message.success('Loaded ' + this.agencyList.length + ' agencies'); 
         } else if (res && res.rows) {
           this.agencyList = res.rows;
         } else if (res && res.result) {
            // Check if result is array or object with records
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

      this.form.resetFields();

      this.$nextTick(() => {
        const fields = [
          'name', 'title', 'companyId',
          'phone', 'email', 'officeNum', 'phoneExt',
          'address', 'shipping', 'currency', 'category', 'remark'
        ];

        let data = {};

        fields.forEach(k => {
          if (this.record[k] !== undefined && this.record[k] !== null) {
            data[k] = this.record[k];
          }
        });

        if (!this.isEdit && defaults) {
          data = { ...data, ...defaults };
        }

        this.form.setFieldsValue(data);

        this.previewLogo = this.record.logo
          ? "data:image/jpeg;base64," + this.record.logo
          : null;
      });
    },

    close() {
      this.visible = false;
    },

    handleFile(e) {
      const file = e.target.files[0];
      const reader = new FileReader();

      reader.onload = r => {
        this.fileData = r.target.result.split(",")[1];
        this.previewLogo = r.target.result;
      };

      reader.readAsDataURL(file);
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
            if (this.fileData) {
              payload.logo = this.fileData;
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
