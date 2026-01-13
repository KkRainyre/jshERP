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
              name="file"
              :action="uploadAction"
              :show-upload-list="false"
              @change="handleLogoChange"
              :headers="tokenHeader"
            >
              <a-button icon="upload">Choose Logo</a-button>
            </a-upload>

            <!-- Preview -->
            <div v-if="logoData" class="logo-preview-box">
              <img :src="logoData" alt="Logo Preview" @error="onLogoPreviewError($event)" />
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
  import { getAction } from '@/api/manage'
  import { autoJumpNextInput } from "@/utils/util"
  import { mixinDevice } from '@/utils/mixin'
  import Vue from 'vue'
  import { ACCESS_TOKEN } from "@/store/mutation-types"

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
  tokenHeader: { 'X-Access-Token': Vue.ls.get(ACCESS_TOKEN) },
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
computed: {
  uploadAction: function () {
    return window._CONFIG['domianURL'] + "/systemConfig/upload?biz=agency";
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

  if (record.logo) {
       // Normalize model.logo to objectKey where possible so we don't store expired signed URLs
       // If record.logo is an objectKey (no http) -> fetch signed preview
       if (record.logo.startsWith('http')) {
           // It might be a signed OSS URL (which can expire). If it belongs to our OSS host, try to extract objectKey
           this.model.logo = record.logo; // temporarily keep raw value
           if (record.logo.indexOf('.aliyuncs.com') > -1) {
               try {
                   const objectKey = record.logo.replace(/^https?:\/\/[^\/]+\/(.*)$/, '$1');
                   // Request fresh signed preview and normalize model.logo to objectKey
                   getAction('/api/oss/urls', { objectKey: objectKey, fileName: '' }).then(res => {
                       if (res && res.previewUrl) {
                           this.logoData = res.previewUrl;
                           this.model.logoUrl = res.previewUrl;
                           this.model.logo = objectKey; // store objectKey for future operations
                       } else {
                           this.logoData = record.logo; // fallback to original
                       }
                   }).catch(err => {
                       console.error('Failed to refresh signed URL for logo', err);
                       this.logoData = record.logo;
                   })
               } catch (e) {
                   console.error('Error extracting objectKey from url', e);
                   this.logoData = record.logo;
               }
           } else {
               // Not an OSS signed url; keep using it directly
               this.logoData = record.logo;
           }
       } else {
          // Assume object key, fetch signed URL from OSS API
          this.model.logo = record.logo;
          getAction('/api/oss/urls', { objectKey: record.logo, fileName: '' }).then(res => {
             if (res && res.previewUrl) {
                 this.logoData = res.previewUrl;
                 this.model.logoUrl = res.previewUrl;
             } else if (res) {
                 const url = res.previewUrl || res;
                 if (url) {
                   this.logoData = url;
                   this.model.logoUrl = url;
                 }
             }
          }).catch(err => {
             console.error('Failed to fetch preview URL', err);
          })
       }
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

  onLogoPreviewError(e) {
    // Called when <img> fails to load. Log failing src and try alternative sources:
    try {
      const failingSrc = e && e.target && e.target.src;
      console.warn('Logo image failed to load:', failingSrc, 'model.logo=', this.model.logo);

      const key = this.model.logo;
      if (key && !String(key).startsWith('http')) {
        // Try OSS urls endpoint
        getAction('/api/oss/urls', { objectKey: key, fileName: '' }).then(res => {
          if (res && res.previewUrl) {
            this.logoData = res.previewUrl;
            this.model.logoUrl = res.previewUrl;
            console.debug('onLogoPreviewError: got previewUrl from /api/oss/urls', res.previewUrl);
            return;
          }
          // Fallback to proxied route
          this.logoData = window._CONFIG['domianURL'] + '/systemConfig/static/' + key;
          this.model.logoUrl = this.logoData;
          console.debug('onLogoPreviewError: fallback to proxied static route', this.logoData);
        }).catch(err => {
          console.error('Fallback: failed to fetch preview URL', err);
          this.logoData = window._CONFIG['domianURL'] + '/systemConfig/static/' + key;
          this.model.logoUrl = this.logoData;
        })
      } else if (key && String(key).startsWith('http')) {
        // If model.logo is already full URL and it failed, try server proxy with full path encoded
        this.logoData = window._CONFIG['domianURL'] + '/systemConfig/static/' + key;
        this.model.logoUrl = this.logoData;
        console.debug('onLogoPreviewError: proxied original http url', this.logoData);
      } else {
        // Nothing we can do — clear preview
        this.logoData = null;
        this.model.logoUrl = null;
      }
    } catch (err) {
      console.error('onLogoPreviewError handler failed', err);
    }
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

  // ------------ LOGO UPLOAD (FINAL) ------------

  handleLogoChange(info) {
    if (info.file.status === 'done') {
      const res = info.file.response;
      // Object key is returned directly (plain string) or in res.data if standard response
      // Based on controller, it returns purely the objectKey string? No, ResponseJsonUtil might wrap it.
      // Wait, SystemConfigService.uploadAliOss returns a String (objectKey).
      // The controller wraps it in BaseResponseInfo? 
      // SystemConfigController.upload returns BaseResponseInfo. res.data = objectKey.
      
      if (res && res.code === 200) {
        // Parse the JSON string from res.data
        let data = res.data;
        if (typeof data === 'string') {
           try {
              data = JSON.parse(data);
           } catch (e) {
              // fallback if it's just a string key
              data = { objectKey: data }; 
           }
        }
        
        const objectKey = data.objectKey || data; 
        const signedUrl = data.signedUrl;

        console.debug('handleLogoChange: upload result', { objectKey, signedUrl, raw: data });

        this.model.logo = objectKey;
        this.model.logoUrl = signedUrl; // Store signed URL in model

        if (signedUrl) {
           this.logoData = signedUrl;
           console.debug('handleLogoChange: set logoData to signedUrl', this.logoData);
        } else {
           console.debug('handleLogoChange: no signedUrl returned, will fetch via OSS API / proxy fallback');
           // Fallback if no signedUrl returned (backward compatibility) — use OSS API
            getAction('/api/oss/urls', { objectKey: objectKey, fileName: '' }).then(res => {
                 if (res && res.previewUrl) {
                     this.logoData = res.previewUrl;
                     this.model.logoUrl = res.previewUrl;
                 } else if (res) {
                     const url = res.previewUrl || res;
                     if (url) {
                       this.logoData = url;
                       this.model.logoUrl = url;
                     }
                 }
            }).catch(err => {
                 console.error('Failed to fetch preview URL', err);
                 // Final fallback: proxy the OSS object via server static endpoint to avoid CORS or signed URL issues
                 try {
                   const proxied = window._CONFIG['domianURL'] + '/systemConfig/static/' + encodeURIComponent(objectKey);
                   this.logoData = proxied;
                   this.model.logoUrl = proxied;
                 } catch (e) {
                   console.error('Failed to set proxied logo url', e);
                 }
            })
        }
        
        this.$message.success('Logo uploaded successfully');
      } else {
        this.$message.warning(res.data || 'Upload failed');
      }
    } else if (info.file.status === 'error') {
      this.$message.error('Upload error');
    }
  },

  // Deleted legacy beforeLogoUpload


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