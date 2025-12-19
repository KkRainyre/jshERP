<template>
  <div ref="container">
    <a-modal
      :title="title"
      :width="1000"
      :visible="visible"
      :confirmLoading="confirmLoading"
      :getContainer="() => $refs.container"
      :maskClosable="false"
      @ok="handleOk"
      @cancel="handleCancel"
      okText="Save"
      cancelText="Cancel"
    >
      <a-spin :spinning="confirmLoading">
        <a-form :form="form">

          <!-- ========================= -->
          <!--   SECTION: BASIC INFO     -->
          <!-- ========================= -->
          <div class="section-title" style="margin-bottom: 12px;"><strong>Basic Information</strong></div>

          <a-row :gutter="24">

            <a-col :span="6">
              <a-form-item label="Agency" :labelCol="{span:8}" :wrapperCol="{span:16}">
                <a-input placeholder="Agency Name"
                         v-decorator.trim="['name', validatorRules.name]" />
              </a-form-item>
            </a-col>

            <a-col :span="7">
              <a-form-item label="Email" :labelCol="{span:8}" :wrapperCol="{span:16}">
                <a-input placeholder="Email" v-decorator.trim="['email', validatorRules.email]" />
              </a-form-item>
            </a-col>

            <a-col :span="6">
              <a-form-item label="Phone" :labelCol="{span:8}" :wrapperCol="{span:16}">
                <a-input placeholder="Phone" v-decorator.trim="['phone', validatorRules.phone]" />
              </a-form-item>
            </a-col>

            <a-col :span="5">
              <a-form-item label="Tier" :labelCol="{span:8}" :wrapperCol="{span:16}">
                <a-select placeholder="Select Tier" v-decorator="['tier', validatorRules.tier]" allowClear>
                  <a-select-option value="A">A</a-select-option>
                  <a-select-option value="B">B</a-select-option>
                  <a-select-option value="C">C</a-select-option>
                  <a-select-option value="D">D</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>

          </a-row>

          <!-- ========================= -->
          <!--   SECTION: ADDRESS        -->
          <!-- ========================= -->
          <div class="section-title" style="margin-bottom: 12px;"><strong>Address Information</strong></div>

          <a-row :gutter="24">

            <a-col :span="9">
              <a-form-item label="Street Address" :labelCol="{span:8}" :wrapperCol="wrapperCol">
                <a-input placeholder="Street Address" v-decorator.trim="['address', validatorRules.address]" />
              </a-form-item>
            </a-col>
            <a-col :span="6" style="margin-top: 32px;">
              <a-checkbox v-model="sameAddress">
                Bill address the same?
              </a-checkbox>
            </a-col>

            <a-col :span="9" v-if="!sameAddress">
              <a-form-item label="Billing Address" :labelCol="{span:8}" :wrapperCol="wrapperCol">
                <a-input placeholder="Billing Address" v-decorator.trim="['billAddress', validatorRules.billAddress]" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row :gutter="24">

          <a-col :span="6">
              <a-form-item label="Country" :labelCol="{span:8}" :wrapperCol="{span:8}">
                <a-select v-decorator="['country', validatorRules.country]" allowClear placeholder="Country"  style="width: 120px">
                  <a-select-option value="United States">United States</a-select-option>
                  <a-select-option value="Canada">Canada</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>

            <a-col :span="6">
              <a-form-item label="Postal" :labelCol="{span:8}" :wrapperCol="{span:8}">
                <a-input
                  placeholder="Postal Code"
                  style="width: 140px"
                  @blur="lookupPostal"
                  v-decorator.trim="['postal', validatorRules.postal]"
                />
              </a-form-item>
            </a-col>


            <a-col :span="6">
              <a-form-item label="City" :labelCol="{span:8}" :wrapperCol="{span:12}">
                <a-input placeholder="City" v-decorator.trim="['city', validatorRules.city]" />
              </a-form-item>
            </a-col>

            <a-col :span="6">
              <a-form-item label="State" :labelCol="{span:8}" :wrapperCol="{span:12}">
                <a-input placeholder="State" v-decorator.trim="['state', validatorRules.state]" />
              </a-form-item>
            </a-col>

          </a-row>

          <!-- ========================= -->
          <!--       SECTION: OTHER      -->
          <!-- ========================= -->
          <div class="section-title" style="margin-bottom: 12px;"><strong>Additional Details</strong></div>
          <a-row :gutter="24">
              <a-col :span="12">
                <a-form-item label="Shipping Account" :labelCol="{span:7}" :wrapperCol="{span:16}">
                  <a-input placeholder="Shipping Account" v-decorator.trim="['shippingAcc', validatorRules.shippingAcc]" />
                </a-form-item>
              </a-col>
            <a-col :span="12">

              <a-form-item label="EIN / TAX Number" :labelCol="{span:8}" :wrapperCol="{span:16}">
                <a-input placeholder="Tax EIN" v-decorator.trim="['EIN']" />
              </a-form-item>
            </a-col>

          </a-row>
          <a-row :gutter="24">

            <a-col :span="12">
              <a-form-item label="Website" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="Website" v-decorator.trim="['website', validatorRules.website]" />
              </a-form-item>
            </a-col>


          </a-row>
          <a-row :gutter="24">
            <a-col :span="24">
              <a-form-item label="Remarks" :labelCol="{span: 2}" :wrapperCol="{ span: 22 }">
                <a-textarea rows="4" placeholder="Notes or remarks" v-decorator.trim="['remarks']" />
              </a-form-item>
            </a-col>

          </a-row>

          <!-- ========================= -->
          <!--        LOGO UPLOAD        -->
          <!-- ========================= -->
          <div class="section-title" style="margin-bottom: 12px;"><strong>Agency Logo</strong></div>

          <a-form-item label="Upload Logo" :labelCol="{span: 3}" :wrapperCol="{span: 21}">
            <a-upload
              accept="image/*"
              :show-upload-list="false"
              :before-upload="beforeLogoUpload"
            >
              <a-button icon="upload">Choose Logo</a-button>
            </a-upload>

            <!-- Preview -->
            <div v-if="logoData" class="logo-preview-box">
              <img :src="logoData" alt="Logo Preview" />
            </div>
          </a-form-item>
        </a-form>
      </a-spin>
    </a-modal>
  </div>

</template>


<script>

  import pick from 'lodash.pick'
  import { addAgency, editAgency, checkAgency } from '@/api/api'
  import { autoJumpNextInput } from "@/utils/util"
  import { mixinDevice } from '@/utils/mixin'

  export default {
  name: "AgencyModal",
  mixins: [mixinDevice],
  data () {
  return {
  title: "操作",
  visible: false,
  model: {},
  logoData: null,
  isReadOnly: false,
  labelCol: {
  xs: { span: 24 },
  sm: { span: 4 },
},
  wrapperCol: {
  xs: { span: 24 },
  sm: { span: 20 },
},
  confirmLoading: false,
  form: this.$form.createForm(this),
    validatorRules: {
      name: {
        rules: [
          { required: true, message: "Agency Name is required" },
          { min: 2, max: 60, message: "Agency Name must be 2–60 characters" }
        ]
      },

      email: {
        rules: [
          { required: true, message: "Email is required" },
          { type: "email", message: "Enter a valid email address" }
        ]
      },

      phone: {
        rules: [
          { required: true, message: "Phone is required" },
          { pattern: /^[0-9+\-\s()]{7,20}$/, message: "Phone format is invalid" }
        ]
      },

      tier: {
        rules: [
          { required: true, message: "Tier is required" }
        ]
      },

      address: {
        rules: [
          { required: true, message: "Street Address is required" }
        ]
      },

      billAddress: {
        rules: [
          { required: true, message: "Billing Address is required when not same as shipping" }
        ]
      },

      country: {
        rules: [
          { required: true, message: "Country is required" }
        ]
      },

      postal: {
        rules: [
          { required: true, message: "Postal Code is required" },
          { min: 3, message: "Postal Code is too short" }
        ]
      },

      city: {
        rules: [
          { required: true, message: "City is required" }
        ]
      },

      state: {
        rules: [
          { required: true, message: "State is required" }
        ]
      },

      shippingAcc: {
        rules: [
          { required: true, message: "Shipping Account is required" }
        ]
      },

      website: {
        rules: [
          { required: true, message: "Website is required" },
          { pattern: /^https?:\/\/.+/i, message: "Website must start with http:// or https://" }
        ]
      }
    }
    ,
  sameAddress: false
}
},
  methods: {
  add() {
  this.edit({});
},

  edit(record) {
  this.form.resetFields();
  this.model = Object.assign({}, record);
  
  this.visible = true;

  // Check if billing address is empty and checking the box if so
  this.sameAddress = !this.model.billAddress;

  this.$nextTick(() => {
  this.form.setFieldsValue(
  pick(this.model, 'name', 'email', 'phone', 'address', 'billAddress', 'country', 'remarks','postal','city','state','shippingAcc','website','EIN', 'tier')
  );
  // autoJumpNextInput('AgencyModal');
});

  // hydrate logo preview from backend
  if (record.logo) {
  let base64;
  if (Array.isArray(record.logo)) {
  base64 = this.arrayBufferToBase64(record.logo);
} else {
  base64 = record.logo;
}
  this.logoData = "data:image/png;base64," + base64;
  this.model.logo = base64;
} else {
  this.logoData = null;
  this.model.logo = null;
}
},

  close() {
  this.$emit('close');
  this.visible = false;
},

  handleOk() {
  const that = this;
  this.form.validateFields((err, values) => {
  if (!err) {
  that.confirmLoading = true;

  // Map country to EXT1 for storage, if country is present
  if (values.country) {
      values.country = values.country;
  }

  

  const formData = Object.assign({}, this.model, values, {
  logo: this.model.logo || null
});


  formData.type = "agency";

  const req = this.model.id ? editAgency(formData) : addAgency(formData);

  req.then((res) => {
  if (res.code === 200) {
  that.$emit('ok');
} else {
  that.$message.warning(res.data.message);
}
}).finally(() => {
  that.confirmLoading = false;
  that.close();
});
}
});
},

  handleCancel() {
  this.close();
},

  validateSupplierName(rule, value, callback) {
  const params = {
  name: value,
  type: 'agency',
  id: this.model.id ? this.model.id : 0
};
  checkSupplier(params).then((res) => {
  if (res && res.code === 200) {
  if (!res.data.status) {
  callback();
} else {
  callback("Agency Name is existed");
}
} else {
  callback(res.data);
}
});
},

  // ------------ LOGO UPLOAD (FINAL) ------------

  beforeLogoUpload(file) {
  const reader = new FileReader();

  reader.onload = (e) => {
  const base64 = e.target.result;
  this.logoData = base64;                 // full data URL for preview
  this.model.logo = base64.split(",")[1]; // raw base64 for backend
};

  reader.readAsDataURL(file);

  // block AntD from auto-uploading
  return false;
},

  arrayBufferToBase64(buffer) {
  let binary = '';
  const bytes = new Uint8Array(buffer);
  const len = bytes.byteLength;
  for (let i = 0; i < len; i++) {
  binary += String.fromCharCode(bytes[i]);
}
  return window.btoa(binary);
},

  cleanCityName(name) {
  if (!name) return "";
  const directions = [" North", " South", " East", " West"];
  let cleaned = name;
  directions.forEach(dir => {
  if (cleaned.endsWith(dir)) {
  cleaned = cleaned.replace(dir, "");
}
});
  return cleaned.trim();
},

  // ------------ POSTAL LOOKUP ------------

  async lookupPostal() {
  const postal = this.form.getFieldValue("postal");
  if (!postal) return;

  const country = (this.form.getFieldValue("country") || "us").toLowerCase();

  const handlers = {
  us: this.lookupUSPostal,
  ca: this.lookupCanadaPostal
};

  const handler = handlers[country];

  if (!handler) {
  this.$message.warning("Postal lookup not supported for this country");
  return;
}

  try {
  const result = await handler(postal);
  if (!result) {
  this.$message.warning("Postal code not found");
  return;
}

  this.form.setFieldsValue(result);
  this.$message.success("Address auto-filled!");
} catch (err) {
  this.$message.error("Postal lookup failed");
}
},

  async lookupCanadaPostal(postal) {
  if (!postal) return null;

  const formatted = postal
  .toUpperCase()
  .replace(/\s+/g, "")
  .replace(/(.{3})(.{3})/, "$1 $2")
    .slice(0,3);   // NO slice(0,3) – use full code

  const res = await fetch(`https://api.zippopotam.us/ca/${formatted}`);
  if (!res.ok) return null;

  const data = await res.json();
  const place = data.places && data.places[0];
  if (!place) return null;

  let city = place["place name"];
  let state = place["state"];

  const suffixes = [" West", " East", " North", " South", "Downtown "];
  suffixes.forEach(s => {
  if (city.endsWith(s)) city = city.replace(s, "");
});

  return {
  city: city.trim(),
  state
};
},

  async lookupUSPostal(postal) {
  const res = await fetch(`https://api.zippopotam.us/us/${postal}`);
  if (!res.ok) return null;

  const data = await res.json();
  const place = data.places[0];

  return {
  city: place["place name"],
  state: place["state"]
};
}
}
}





</script>
<style scoped>
.logo-preview-box {
  margin-top: 12px;
  width: 100%;
  max-height: 260px;     /* safe height inside modal */
  overflow-y: auto;      /* show entire image */
  border: 1px solid #e8e8e8;
  border-radius: 4px;
  padding: 8px;
  background: #fafafa;
}

.logo-preview-box img {
  width: 100%;
  height: auto;
  display: block;
  object-fit: contain;
  image-orientation: from-image; /* auto fix rotation */
}



</style>