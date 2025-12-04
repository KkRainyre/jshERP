<template>
  <div ref="container">
    <a-modal
      :title="title"
      :width="1200"
      :visible="visible"
      :confirmLoading="confirmLoading"
      :getContainer="() => $refs.container"
      :maskStyle="{'top':'93px','left':'154px'}"
      :wrapClassName="wrapClassNameInfo()"
      :mask="isDesktop()"
      :maskClosable="false"
      @ok="handleOk"
      @cancel="handleCancel"
      cancelText="取消"
      okText="保存"
      style="top:15%;height: 60%;">
      <template slot="footer">
        <a-button key="back" v-if="isReadOnly" @click="handleCancel">
          取消
        </a-button>
      </template>
      <a-spin :spinning="confirmLoading">
        <a-form :form="form" id="memberModal">
          <a-row class="form-row" :gutter="24">
            <a-col :span="24/2">
              <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="Agency">
                <a-input placeholder="Please enter your Agency Name" v-decorator.trim="[ 'supplier', validatorRules.supplier]" />
              </a-form-item>
            </a-col>
            <a-col :span="24/2">
              <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="Contact Person">
                <a-input placeholder="Please enter your Contact Person" v-decorator.trim="[ 'contacts' ]" />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row class="form-row" :gutter="24">
            <a-col :span="24/2">
              <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="Contact">
                <a-input placeholder="Please enter contact number" v-decorator.trim="[ 'telephone' ]" />
              </a-form-item>
            </a-col>
            <a-col :span="24/2">
              <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="Address">
                <a-input placeholder="Please enter address" v-decorator.trim="[ 'phoneNum' ]" />
              </a-form-item>
            </a-col>
            <a-col :span="24/2">
              <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="E-mail">
                <a-input placeholder="Please enter email" v-decorator.trim="[ 'email' ]" />
              </a-form-item>
            </a-col>
            <a-col :span="24/2">
              <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="Currency">
                <a-input placeholder="Please enter currency" v-decorator.trim="[ 'sort' ]" />
              </a-form-item>
            </a-col>
            <a-col :span="24/2">
              <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="Remarks">
                <a-textarea :rows="2" placeholder="Please enter the remarks" v-decorator.trim="[ 'description' ]" />
              </a-form-item>

              <!-- LOGO UPLOADER -->
<!--              //marco-->
              <a-row>
                <a-col :span="24">
                  <div class="logo-uploader">

                    <label>Upload Logo:</label>
                    <input type="file" accept="image/*" @change="handleFile" />

                    <div v-if="logoData" style="margin-top:10px;">
                      <h4>Preview:</h4>
                      <img :src="logoData" class="preview" style="max-width:200px; border:1px solid #ccc;">
                    </div>

                    <a-button type="primary" style="margin-top:10px;" @click="saveLogo">
                      Save Logo
                    </a-button>

                  </div>
                </a-col>
              </a-row>
<!--              //marco-->
            </a-col>
          </a-row>
        </a-form>
      </a-spin>
    </a-modal>
  </div>
</template>
<script>
  import pick from 'lodash.pick'
  import {addSupplier,editSupplier,checkSupplier } from '@/api/api'
  import {autoJumpNextInput} from "@/utils/util"
  import {mixinDevice} from '@/utils/mixin'
  export default {
    name: "AgencyModal",
    mixins: [mixinDevice],
    data () {
      return {
        title:"操作",
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
        validatorRules:{
          supplier:{
            rules: [
              { required: true, message: '请输入会员卡号!' },
              { min: 2, max: 60, message: '长度在 2 到 60 个字符', trigger: 'blur' },
              { validator: this.validateSupplierName}
            ]
          }
        },
      }
    },
    created () {
    },
    methods: {
      add() {
        this.edit({});
      },
      edit(record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'supplier', 'contacts', 'telephone', 'email',
            'phoneNum', 'sort', 'description'))
          autoJumpNextInput('memberModal')
        });
        if (record.logo) {

          let base64;

          if (Array.isArray(record.logo)) {
            // backend returned byte[]
            base64 = this.arrayBufferToBase64(record.logo);
          } else {
            // backend returned Base64 string
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
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            // let formData = Object.assign(this.model, values);

            let formData = Object.assign({}, this.model, values, {
              logo: this.model.logo || null
            });
            //after change
            if (this.model.beginNeedGet && this.model.beginNeedPay) {
              that.$message.warn("期初应收和期初应付不能同时输入");
              that.confirmLoading = false;
              return;
            }
            formData.type = "会员";
            let obj;
            if (!this.model.id) {
              obj = addSupplier(formData);
            } else {
              obj = editSupplier(formData);
            }
            obj.then((res) => {
              if (res.code === 200) {
                that.$emit('ok');
              } else {
                that.$message.warning(res.data.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }
        })
      },
      handleCancel() {
        this.close()
      },
      validateSupplierName(rule, value, callback) {
        let params = {
          name: value,
          type: '会员',
          id: this.model.id ? this.model.id : 0
        };
        checkSupplier(params).then((res) => {
          if (res && res.code === 200) {
            if (!res.data.status) {
              callback();
            } else {
              callback("会员卡号已经存在");
            }
          } else {
            callback(res.data);
          }
        });
      },
      // marco logo -add
      handleFile(e) {
        const file = e.target.files[0];
        if (!file) return;

        const reader = new FileReader();
        reader.onload = (event) => {
          this.logoData = event.target.result;

          // AUTO-SAVE LOGO (no more save button)
          const base64 = this.logoData.split(",")[1];
          this.model.logo = base64;
        };
        reader.readAsDataURL(file);
      },


      saveLogo() {
        if (!this.logoData) {
          this.$message.warning("No logo selected!");
          return;
        }

        // Extract only Base64 part ("data:image/png;base64,....")
        const base64 = this.logoData.split(",")[1];
        this.model.logo = base64;

        this.$message.success("Logo added to form!");
      },

    },

    arrayBufferToBase64(buffer) {
      let binary = '';
      let bytes = new Uint8Array(buffer);
      let len = bytes.byteLength;
      for (let i = 0; i < len; i++) {
        binary += String.fromCharCode(bytes[i]);
      }
      return window.btoa(binary);
    }

  }



</script>
<style scoped>

</style>