<template>
  <a-modal
    :visible="visible"
    :title="isEdit ? 'Edit Quote' : 'New Quote'"
    :confirmLoading="confirmLoading"
    :maskClosable="false"
    width="98%"
    @ok="handleOk"
    @cancel="handleCancel"
  >
    <a-form :form="form" layout="vertical">
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="Quote Reference">
            <a-input
              v-decorator="[
                'quoteNo',
                {
                  rules: [
                    { required: true, message: 'Please input Quote Reference!' },
                    { max: 50, message: 'Max 50 characters' }
                  ]
                }
              ]"
              placeholder="e.g. Q2023-001"
              disabled
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="Agency">
            <a-select 
              v-decorator="['agencyId', { rules: [{ required: true, message: 'Please select Agency!' }] }]"
              placeholder="Select Agency"
              show-search
              optionFilterProp="children"
              @change="handleAgencyChange"
            >
              <a-select-option v-for="item in agencyList" :key="item.id" :value="item.id">
                {{ item.name }}
              </a-select-option>
            </a-select>
            <!-- Hidden Fields for mapping -->
            <a-form-item v-show="false">
               <a-input v-decorator="['customerId']" />
               <a-input v-decorator="['customerName']" />
            </a-form-item>
          </a-form-item>
        </a-col>

        <a-col :span="12">
          <a-form-item label="Project">
             <a-select 
                v-model="projectName"
                placeholder="Select Project"
                show-search
                optionFilterProp="children"
             >
                <a-select-option v-for="p in projectList" :key="p.id" :value="p.name">
                   {{ p.name }}
                </a-select-option>
             </a-select>
          </a-form-item>
        </a-col>

        <a-col :span="12">
          <a-form-item label="Status">
            <a-select 
              v-decorator="['status', { initialValue: 'Draft' }]"
              placeholder="Select Status"
            >
              <a-select-option value="Draft">Draft</a-select-option>
              <a-select-option value="Pending">Pending</a-select-option>
              <a-select-option value="Approved">Approved</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <!-- Hidden currency field to ensure it is submitted -->
        <a-form-item v-show="false">
          <a-input v-decorator="['currency', { initialValue: 'CAD' }]" />
        </a-form-item>

        <!-- Spec Code Generator Section -->
        <a-col :span="24">
          <a-card title="Specification Code Generator" size="small" style="margin-bottom: 20px; background: #f5f5f5;">
            <a-row :gutter="8" style="margin-bottom: 10px">
               <a-col :span="6">
                  <div class="code-label">Product Family</div>
                  <a-select v-model="selectedProduct" @change="handleProductChange" style="width: 100%">
                    <a-select-option v-for="(cfg, key) in productConfigs" :key="key" :value="key">
                      {{ cfg.label }}
                    </a-select-option>
                  </a-select>
               </a-col>
            </a-row>

            <a-row :gutter="8">
               <!-- Prefix -->
               <a-col :span="2">
                 <div class="code-label">Prefix</div>
                 <a-input :value="currentConfig.prefix" disabled />
               </a-col>

               <!-- Shape -->
               <a-col :span="3">
                 <div class="code-label">Shape</div>
                 <a-select v-model="generator.shape" placeholder="Shape">
                    <a-select-option v-for="opt in currentOptions.shape" :key="opt.value" :value="opt.value">
                       {{ opt.label }}
                    </a-select-option>
                 </a-select>
               </a-col>

               <!-- Dimension -->
               <a-col :span="3">
                 <div class="code-label">Dimension</div>
                 <a-input v-model="generator.dimension" placeholder="WxL" />
               </a-col>

               <!-- CCT -->
               <a-col :span="2">
                 <div class="code-label">CCT</div>
                 <a-select v-model="generator.cct" placeholder="CCT">
                   <a-select-option v-for="opt in currentOptions.cct" :key="opt.value" :value="opt.value">
                       {{ opt.label }}
                   </a-select-option>
                 </a-select>
               </a-col>

               <!-- Control -->
               <a-col :span="2">
                 <div class="code-label">Control</div>
                 <a-select v-model="generator.control" placeholder="Ctrl">
                   <a-select-option v-for="opt in currentOptions.control" :key="opt.value" :value="opt.value">
                       {{ opt.label }}
                   </a-select-option>
                 </a-select>
               </a-col>

               <!-- Profile -->
               <a-col :span="2">
                 <div class="code-label">Profile</div>
                 <a-select v-model="generator.profile" placeholder="Prof">
                   <a-select-option v-for="opt in currentOptions.profile" :key="opt.value" :value="opt.value">
                       {{ opt.label }}
                   </a-select-option>
                 </a-select>
               </a-col>

               <!-- Color -->
               <a-col :span="3">
                 <div class="code-label">Color</div>
                 <a-select v-model="generator.color" placeholder="Color">
                   <a-select-option v-for="opt in currentOptions.color" :key="opt.value" :value="opt.value">
                       {{ opt.label }}
                   </a-select-option>
                 </a-select>
               </a-col>

               <!-- Mounting -->
               <a-col :span="3">
                 <div class="code-label">Mounting</div>
                 <a-select v-model="generator.mounting" placeholder="Mnt">
                   <a-select-option v-for="opt in currentOptions.mounting" :key="opt.value" :value="opt.value">
                       {{ opt.label }}
                   </a-select-option>
                 </a-select>
               </a-col>

               <!-- Suffix -->
               <a-col :span="2">
                 <div class="code-label">Suffix</div>
                 <a-input :value="currentConfig.suffix" disabled />
               </a-col>
               
               <!-- Generate Button -->
               <a-col :span="2">
                 <div class="code-label" style="visibility: hidden">Action</div>
                 <a-button type="primary" icon="download" @click="addFromGenerator">Add</a-button>
               </a-col>
            </a-row>
            <a-row style="margin-top: 10px">
              <a-col :span="24">
                <strong>Preview: </strong> <span style="font-family: monospace; font-size: 16px; color: #1890ff; margin-right: 10px;">{{ generatedSpecCode }}</span>
                <a-button size="small" icon="copy" @click="copyCode">Copy</a-button>
              </a-col>
            </a-row>
          </a-card>
        </a-col>

        <!-- Application Section -->
        <a-col :span="24">
          <a-card title="Application" size="small" style="margin-bottom: 20px; background: #f5f5f5;">
             <a-row :gutter="24" type="flex" align="middle">
                 <a-col :span="6">
                     <a-form-item label="Application Type" style="margin-bottom: 0">
                         <a-select v-model="applicationType" style="width: 100%">
                             <a-select-option value="Wall">Wall</a-select-option>
                             <a-select-option value="Ceiling">Ceiling</a-select-option>
                         </a-select>
                     </a-form-item>
                 </a-col>
                 <a-col :span="6">
                     <a-form-item label="Measurement" style="margin-bottom: 0">
                         <a-select v-model="measurementUnit" style="width: 100%" placeholder="Unit">
                             <a-select-option value="Feet">Feet</a-select-option>
                             <a-select-option value="Inches">Inches</a-select-option>
                             <a-select-option value="Mm">mm</a-select-option>
                             <a-select-option value="Cm">cm</a-select-option>
                         </a-select>
                     </a-form-item>
                 </a-col>
                 <a-col :span="6">
                   <a-form-item label="X (Horizontal)" style="margin-bottom: 0">
                      <a-input v-model="applicationDimensions.x" placeholder=" Enter Horizontal Dimension " @blur="checkDimensions" />
                   </a-form-item>
                </a-col>
                <a-col :span="6">
                   <a-form-item label="Y (Vertical)" style="margin-bottom: 0">
                      <a-input v-model="applicationDimensions.y" placeholder=" Enter Vertical Dimension " @blur="checkDimensions" />
                   </a-form-item>
                </a-col>
             </a-row>
          </a-card>
        </a-col>

        <!-- Hardware and Light Split Section -->
        <a-col :span="24" style="margin-bottom: 20px;">
           <a-row :gutter="16">
              <a-col :span="12">
                 <a-card title="Hardware" size="small" style="background: #f9f9f9;">
                    <a-row :gutter="8">
                       <a-col :span="12">
                          <a-form-item label="Profile" style="margin-bottom: 8px">
                             <a-select v-model="hardwareConfig.profile" placeholder="Profile">
                                <a-select-option value="P1">Profile 1</a-select-option>
                                <a-select-option value="P2">Profile 2</a-select-option>
                             </a-select>
                          </a-form-item>
                       </a-col>
                       <a-col :span="12">
                          <a-form-item label="Mounting" style="margin-bottom: 8px">
                             <a-select v-model="hardwareConfig.mounting" placeholder="Mounting">
                                <a-select-option value="Recessed">Recessed</a-select-option>
                                <a-select-option value="Surface Mounted">Surface Mounted</a-select-option>
                                <a-select-option value="Suspended">Suspended</a-select-option>
                             </a-select>
                          </a-form-item>
                       </a-col>
                       <a-col :span="12">
                           <a-row :gutter="8">
                             <a-col :span="24">
                                 <span style="font-size: 14px; color: rgba(0,0,0,0.85); display: inline-block; margin-bottom: 8px">Split - Horizontal</span>
                             </a-col>
                             <a-col :span="12">
                                 <a-form-item style="margin-bottom: 8px">
                                     <a-input v-model="hardwareConfig.splitHorizontalQuantity" placeholder="Qty" />
                                 </a-form-item>
                             </a-col>
                             <a-col :span="12">
                                 <a-form-item style="margin-bottom: 8px">
                                     <a-input v-model="hardwareConfig.splitHorizontalLength" placeholder="Len" />
                                 </a-form-item>
                             </a-col>
                             <a-col :span="24">
                                 <span style="font-size: 14px; color: rgba(0,0,0,0.85); display: inline-block; margin-bottom: 8px">Split - Vertical</span>
                             </a-col>
                             <a-col :span="12">
                                 <a-form-item style="margin-bottom: 8px">
                                     <a-input v-model="hardwareConfig.splitVerticalQuantity" placeholder="Qty" />
                                 </a-form-item>
                             </a-col>
                             <a-col :span="12">
                                 <a-form-item style="margin-bottom: 8px">
                                     <a-input v-model="hardwareConfig.splitVerticalLength" placeholder="Len" />
                                 </a-form-item>
                             </a-col>
                           </a-row>
                        </a-col>
                       <a-col :span="12">
                          <a-row :gutter="8">
                             <a-col :span="24">
                                <span style="font-size: 14px; color: rgba(0,0,0,0.85); display: inline-block; margin-bottom: 8px">
                                   Crossbeam - Horizontal
                                   <span v-if="Number(applicationDimensions.x) >= Number(applicationDimensions.y)" style="color: #1890ff; font-weight: bold; margin-left: 8px;">(Main Crossbeam)</span>
                                </span>
                             </a-col>
                             <a-col :span="12">
                                <a-form-item style="margin-bottom: 8px">
                                   <a-input v-model="hardwareConfig.crossbeamHorizontalQuantity" placeholder="Qty" />
                                </a-form-item>
                             </a-col>
                             <a-col :span="12">
                                <a-form-item style="margin-bottom: 8px">
                                   <a-input v-model="hardwareConfig.crossbeamHorizontalLength" placeholder="Len" />
                                </a-form-item>
                             </a-col>
                             <a-col :span="24">
                                <span style="font-size: 14px; color: rgba(0,0,0,0.85); display: inline-block; margin-bottom: 8px">
                                   Crossbeam - Vertical
                                   <span v-if="Number(applicationDimensions.y) > Number(applicationDimensions.x)" style="color: #1890ff; font-weight: bold; margin-left: 8px;">(Main Crossbeam)</span>
                                </span>
                             </a-col>
                             <a-col :span="12">
                                <a-form-item style="margin-bottom: 8px">
                                   <a-input v-model="hardwareConfig.crossbeamVerticalQuantity" placeholder="Qty" />
                                </a-form-item>
                             </a-col>
                             <a-col :span="12">
                                <a-form-item style="margin-bottom: 8px">
                                   <a-input v-model="hardwareConfig.crossbeamVerticalLength" placeholder="Len" />
                                </a-form-item>
                             </a-col>
                          </a-row>
                       </a-col>
                    </a-row>
                 </a-card>
              </a-col>
              <a-col :span="12">
                  <a-card title="Light" size="small" style="background: #f9f9f9;">
                     <a-row :gutter="8">
                        <a-col :span="12">
                           <a-form-item label="Pitch" style="margin-bottom: 8px">
                              <a-select v-model="lightConfig.pitch" placeholder="Pitch">
                                 <a-select-option value="P1.2">P1.2</a-select-option>
                                 <a-select-option value="P1.5">P1.5</a-select-option>
                                 <a-select-option value="P2.0">P2.0</a-select-option>
                              </a-select>
                           </a-form-item>
                        </a-col>
                        <a-col :span="12">
                           <a-form-item label="CCT" style="margin-bottom: 8px">
                              <a-select v-model="lightConfig.cct" placeholder="CCT">
                                 <a-select-option value="3000K">3000K</a-select-option>
                                 <a-select-option value="4000K">4000K</a-select-option>
                                 <a-select-option value="6000K">6000K</a-select-option>
                              </a-select>
                           </a-form-item>
                        </a-col>
                        <a-col :span="12">
                           <a-form-item label="Wattage" style="margin-bottom: 8px">
                              <a-select v-model="lightConfig.wattage" placeholder="Wattage">
                                 <a-select-option value="10W">10W</a-select-option>
                                 <a-select-option value="20W">20W</a-select-option>
                                 <a-select-option value="50W">50W</a-select-option>
                              </a-select>
                           </a-form-item>
                        </a-col>
                        <a-col :span="12">
                           <a-form-item label="Lumen Output" style="margin-bottom: 8px">
                              <a-select v-model="lightConfig.lumenOutput" placeholder="Lumen">
                                 <a-select-option value="1000lm">1000lm</a-select-option>
                                 <a-select-option value="2000lm">2000lm</a-select-option>
                                 <a-select-option value="5000lm">5000lm</a-select-option>
                              </a-select>
                           </a-form-item>
                        </a-col>
                        <a-col :span="12">
                           <a-form-item label="Amount of Led" style="margin-bottom: 8px">
                              <a-select v-model="lightConfig.ledAmount" placeholder="LED Count">
                                 <a-select-option value="60">60</a-select-option>
                                 <a-select-option value="120">120</a-select-option>
                                 <a-select-option value="240">240</a-select-option>
                              </a-select>
                           </a-form-item>
                        </a-col>
                     </a-row>
                  </a-card>
              </a-col>
           </a-row>
           
           <!-- Electrical and Diffuser Split Section -->
           <a-row :gutter="16" style="margin-top: 16px;">
              <a-col :span="12">
                 <a-card title="Electrical" size="small" style="background: #f9f9f9;">
                    <!-- Drivers -->
                    <a-row :gutter="8" style="margin-bottom: 8px">
                       <a-col :span="14">
                          <a-select v-model="electricalConfig.driverType" placeholder="Drivers">
                             <a-select-option value="MeanWell">MeanWell</a-select-option>
                             <a-select-option value="Philips">Philips</a-select-option>
                             <a-select-option value="Tridonic">Tridonic</a-select-option>
                          </a-select>
                       </a-col>
                       <a-col :span="10">
                          <a-input-number v-model="electricalConfig.driverCount" placeholder="# of drivers" style="width: 100%" :min="0" />
                       </a-col>
                    </a-row>
                    
                    <!-- Dimming -->
                    <a-row :gutter="8" style="margin-bottom: 8px">
                       <a-col :span="14">
                          <a-select v-model="electricalConfig.dimmingType" placeholder="Dimming">
                             <a-select-option value="DALI">DALI</a-select-option>
                             <a-select-option value="0-10V">0-10V</a-select-option>
                             <a-select-option value="Triac">Triac</a-select-option>
                          </a-select>
                       </a-col>
                       <a-col :span="10">
                          <a-input-number v-model="electricalConfig.controllerCount" placeholder="# controllers" style="width: 100%" :min="0" />
                       </a-col>
                    </a-row>

                    <!-- AC Box -->
                    <a-row :gutter="8" style="margin-bottom: 8px; display: flex; align-items: center;">
                       <a-col :span="14" style="text-align: right; padding-right: 8px;">
                          AC junction box
                       </a-col>
                       <a-col :span="10">
                           <a-input-number v-model="electricalConfig.acBoxCount" placeholder="#" style="width: 100%" :min="0" />
                       </a-col>
                    </a-row>

                    <!-- DC Box -->
                    <a-row :gutter="8" style="margin-bottom: 0; display: flex; align-items: center;">
                       <a-col :span="14" style="text-align: right; padding-right: 8px;">
                          DC junction box
                       </a-col>
                       <a-col :span="10">
                           <a-input-number v-model="electricalConfig.dcBoxCount" placeholder="#" style="width: 100%" :min="0" />
                       </a-col>
                    </a-row>
                 </a-card>
              </a-col>
              <a-col :span="12">
                 <a-card title="Diffuser" size="small" style="background: #f9f9f9;">
                    <a-form-item label="Type" style="margin-bottom: 12px">
                       <a-select v-model="diffuserConfig.type" placeholder="Select Type">
                          <a-select-option value="Stretch ceiling">Stretch ceiling</a-select-option>
                          <a-select-option value="Perforated metal">Perforated metal</a-select-option>
                          <a-select-option value="Fabric ceiling">Fabric ceiling</a-select-option>
                       </a-select>
                    </a-form-item>
                    
                    <a-form-item style="margin-bottom: 0">
                       <div style="display: flex; align-items: center;">
                          <a-checkbox v-model="diffuserConfig.hasBlockout">block out</a-checkbox>
                          <a-input 
                             v-if="diffuserConfig.hasBlockout" 
                             v-model="diffuserConfig.blockoutDimensions" 
                             placeholder="dimensions of blockout" 
                             style="margin-left: 8px; flex: 1" 
                          />
                       </div>
                    </a-form-item>
                 </a-card>
              </a-col>
           </a-row>
        </a-col>

        <a-col :span="24">
           <h3>Product Items</h3>
           <a-button type="dashed" style="width: 100%; margin-bottom: 8px" @click="addItem">
             <a-icon type="plus" /> Add Product
           </a-button>
           
           <a-table
             :columns="paymentTableColumns"
             :dataSource="items"
             :pagination="false"
             rowKey="tempId"
             size="small"
             bordered
           >
             <template slot="itemName" slot-scope="text, record">
               <a-input v-model="record.itemName" placeholder="Item Name" />
             </template>
             <template slot="description" slot-scope="text, record">
               <div style="display: flex; align-items: center;">
                 <a-input v-model="record.description" placeholder="Description" style="flex: 1; margin-right: 4px;" />
                 <a-tooltip title="Copy Specs from Generator">
                   <a-button icon="snippets" size="small" style="margin-right: 4px" @click="copySpecsToDescription(record)" />
                 </a-tooltip>
                 <a-button icon="edit" size="small" @click="openDescriptionEditor(record)" />
               </div>
             </template>
             <template slot="quantity" slot-scope="text, record">
               <a-input-number v-model="record.quantity" :min="1" @change="onQtyChange(record)" style="width: 100%" />
             </template>
             <template slot="unitPrice" slot-scope="text, record">
               <a-input-number v-model="record.unitPrice" :min="0" @change="onPriceChange(record)" style="width: 100%" />
             </template>
             <template slot="lineTotal" slot-scope="text, record">
               <span>{{ (record.quantity * record.unitPrice).toFixed(2) }}</span>
             </template>
             <template slot="action" slot-scope="text, record, index">
                <a-popconfirm title="Sure to delete?" @confirm="deleteItem(index)">
                  <a-icon type="delete" style="color: red; cursor: pointer"/>
                </a-popconfirm>
             </template>
           </a-table>
        <br/>
        </a-col>

        <a-col :span="24">
            <a-row type="flex" justify="space-between" align="middle">
              <!-- Left Side: Location -->
              <a-col :span="10">
                <a-row :gutter="8">
                  <a-col :span="12">
                    <a-form-item label="Country" style="margin-bottom: 0;">
                       <a-select v-model="targetCountry" @change="handleCountryChange">
                          <a-select-option value="CA">Canada</a-select-option>
                          <a-select-option value="US">USA</a-select-option>
                       </a-select>
                    </a-form-item>
                  </a-col>
                  <a-col :span="12">
                    <a-form-item label="State/Province" style="margin-bottom: 0;">
                       <a-select v-model="targetProvince" @change="handleProvinceChange" show-search optionFilterProp="children">
                          <a-select-option v-for="(rate, key) in currentProvinces" :key="key" :value="key">
                             {{ key }}
                          </a-select-option>
                       </a-select>
                    </a-form-item>
                  </a-col>
                </a-row>
              </a-col>

              <!-- Right Side: Financials -->
              <a-col :span="13" style="text-align: right;">
                 <a-row :gutter="16" type="flex" align="bottom">
                     <a-col :span="8" style="padding-right: 20px;">
                        <a-form-item style="margin-bottom: 0;">
                           <span slot="label" style="display: block; text-align: right;">Subtotal</span>
                           <span style="font-size: 16px;">{{ targetCountry === 'CA' ? 'C$' : '$' }}{{ subTotal.toFixed(2) }}</span>
                        </a-form-item>
                     </a-col>
                     <a-col :span="8">
                        <a-form-item style="margin-bottom: 0;">
                           <template slot="label">
                               <div style="display: flex; justify-content: flex-end; align-items: center;">
                                   <a-checkbox v-model="isTaxFree" @change="calculateTotal" style="margin-right: 8px; font-size: 12px; font-weight: normal;">Tax-free</a-checkbox>
                                   <span>Tax ({{ ((isTaxFree ? 0 : taxRate) * 100).toFixed(2) }}%)</span>
                               </div>
                           </template>
                           <span style="font-size: 16px;">{{ targetCountry === 'CA' ? 'C$' : '$' }}{{ taxAmount.toFixed(2) }}</span>
                        </a-form-item>
                     </a-col>
                    <a-col :span="8">
                        <a-form-item style="margin-bottom: 0;">
                          <span slot="label" style="display: block; text-align: right;">Total Amount</span>
                          <span style="font-size: 18px; font-weight: bold; color: #52c41a">{{ targetCountry === 'CA' ? 'C$' : '$' }}{{ form.getFieldValue('totalAmount') }}</span>
                          <a-input-number v-show="false" v-decorator="['totalAmount']"/>
                        </a-form-item>
                    </a-col>
                 </a-row>
              </a-col>
            </a-row>
             <!-- Hidden Ext3 for Operator -->
             <a-form-item v-show="false">
               <a-input v-decorator="['ext3']" />
             </a-form-item>
        </a-col>

      </a-row>

      <!-- Attachments Section -->
      <a-row>
          <a-col :span="24">
             <a-card title="Attachments" size="small" style="margin-top: 20px; background: #f5f5f5;">
               <div style="margin-bottom: 10px;">
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
               
               <a-list v-if="fileList.length > 0" item-layout="horizontal" :data-source="fileList" size="small">
                  <a-list-item slot="renderItem" slot-scope="item, index">
                     <a slot="actions" :href="getFileUrl(item.url)" target="_blank">Download</a>
                     <a slot="actions" style="color: red;" @click="deleteFile(index)">Delete</a>
                     <a-list-item-meta :description="item.date">
                        <a slot="title" :href="getFileUrl(item.url)" target="_blank">{{ item.name }}</a>
                        <a-avatar slot="avatar" icon="file" style="background-color: #faad14" size="small"/>
                     </a-list-item-meta>
                  </a-list-item>
               </a-list>
               <div v-else style="color: #ccc; font-style: italic;">No attachments.</div>
             </a-card>
          </a-col>
      </a-row>
    </a-form>
    
    <!-- Description Editor Modal -->
    <a-modal
      title="Edit Description"
      :visible="editorVisible"
      @ok="handleEditorOk"
      @cancel="editorVisible = false"
      width="600px"
    >
      <a-textarea v-model="editorContent" :rows="10" placeholder="Enter detailed description..." />
    </a-modal>
  </a-modal>
</template>

<script>
import { axios as request } from '@/utils/request'
import { getAction } from '@/api/manage'
import { mapGetters } from 'vuex'
import pick from 'lodash.pick'
import moment from "moment"
import Vue from 'vue'
import { ACCESS_TOKEN } from "@/store/mutation-types"

export default {
  name: 'QuoteModal',
  data () {
    return {
      visible: false,
      confirmLoading: false,
      isEdit: false,
      model: {},
      form: this.$form.createForm(this),
      items: [],
      // Dropdown Data
      agencyList: [],
      projectList: [],
      projectName: undefined, // Manually bound project name
      fileList: [], // Attachments
      uploadAction: window._CONFIG['domianURL'] + '/systemConfig/upload?biz=quote',
      tokenHeader: { 'X-Access-Token': Vue.ls.get(ACCESS_TOKEN) },
      // Generator State
      selectedProduct: 'LumosCielo',
      productConfigs: {
        'LumosCielo': {
           label: 'LumosCielo (WW)',
           prefix: 'BAWW',
           suffix: 'LU',
           options: {
             shape: [
               { value: 'SQ', label: 'SQ (Square)' },
               { value: 'RE', label: 'RE (Rectangle)' },
               { value: 'TR', label: 'TR (Triangle)' },
               { value: 'RO', label: 'RO (Round)' },
               { value: 'CU', label: 'CU (Custom)' }
             ],
             cct: [{ value: 'WW', label: 'WW' }],
             control: [{ value: 'DM', label: 'DM' }],
             profile: ['076', '105', '120', '140'].map(v => ({ value: v, label: v })),
             color: [
               { value: 'Cl', label: 'Cl (Clear)' },
               { value: 'Wh', label: 'Wh (White)' },
               { value: 'Bl', label: 'Bl (Black)' },
               { value: 'Ra', label: 'Ra (#RAL)' }
             ],
             mounting: [
               { value: 'Sf', label: 'Sf (Surface)' },
               { value: 'Su', label: 'Su (Suspended)' },
               { value: 'Re', label: 'Re (Recessed)' }
             ]
           }
        },
        'LumosTerra': {
           label: 'LumosTerra (BATR)',
           prefix: 'BATR',
           suffix: 'LU',
           options: {
             shape: [
               { value: 'LN', label: 'LN (Linear)' },
               { value: 'CR', label: 'CR (Curve)' }
             ],
             cct: [
                { value: '30K', label: '30K (3000K)' },
                { value: '40K', label: '40K (4000K)' }
             ],
             control: [
                { value: 'ND', label: 'ND (Non-Dim)' },
                { value: '0-10', label: '0-10 (0-10V)' }
             ],
             profile: ['050', '080'].map(v => ({ value: v, label: v })),
             color: [
               { value: 'Wh', label: 'Wh (White)' },
               { value: 'Bk', label: 'Bk (Black)' }
             ],
             mounting: [
               { value: 'Sf', label: 'Sf (Surface)' }
             ]
           }
        }
      },
      generator: {
        shape: undefined,
        dimension: '',
        cct: undefined,
        control: undefined,
        profile: undefined,
        color: undefined,
        mounting: undefined
      },
      paymentTableColumns: [
        { title: 'Item Name', dataIndex: 'itemName', width: 300, scopedSlots: { customRender: 'itemName' } },
        { title: 'Description', dataIndex: 'description', scopedSlots: { customRender: 'description' } },
        { title: 'Qty', dataIndex: 'quantity', width: 80, scopedSlots: { customRender: 'quantity' } },
        { title: 'Unit Price', dataIndex: 'unitPrice', width: 120, scopedSlots: { customRender: 'unitPrice' } },
        { title: 'Total', dataIndex: 'lineTotal', width: 120, scopedSlots: { customRender: 'lineTotal' } },
        { title: 'Action', dataIndex: 'action', width: 60, align: 'center', scopedSlots: { customRender: 'action' } }
      ],
      // Tax Logic
      targetCountry: 'CA',
      targetProvince: 'Ontario',
      subTotal: 0,
      taxAmount: 0,
      isTaxFree: false,
      taxRate: 0.13,
      taxRates: {
        'CA': {
           'Alberta': 0.05,
           'British Columbia': 0.12,
           'Manitoba': 0.12,
           'New Brunswick': 0.15,
           'Newfoundland and Labrador': 0.15,
           'Northwest Territories': 0.05,
           'Nova Scotia': 0.15,
           'Nunavut': 0.05,
           'Ontario': 0.13,
           'Prince Edward Island': 0.15,
           'Quebec': 0.14975,
           'Saskatchewan': 0.11,
           'Yukon': 0.05
        },
        'US': {
           'California': 0.0725,
           'New York': 0.088,
           'Texas': 0.0625,
           'Florida': 0.06,
           'Illinois': 0.0625,
           'Pennsylvania': 0.06,
           'Ohio': 0.0575,
           'Georgia': 0.04,
           'North Carolina': 0.0475
           // Add more as needed
        }
      },
      // Editor State
      editorVisible: false,
      editorContent: '',
      currentEditingItem: null,
      // Hardware Section Config
      hardwareConfig: {
       profile: undefined,
       mounting: undefined,
       splitHorizontalQuantity: '',
       splitHorizontalLength: '',
       splitVerticalQuantity: '',
       splitVerticalLength: '',
       crossbeamHorizontalQuantity: '',
       crossbeamHorizontalLength: '',
       crossbeamVerticalQuantity: '',
       crossbeamVerticalLength: ''
    },
      lightConfig: {
         pitch: undefined,
         cct: undefined,
         wattage: undefined,
         lumenOutput: undefined,
         ledAmount: undefined
      },
      electricalConfig: {
         driverType: undefined,
         driverCount: undefined,
         dimmingType: undefined,
         controllerCount: undefined,
         acBoxCount: undefined,
         dcBoxCount: undefined
      },
      mainCrossbeamDirection: 'Horizontal', // Track current main direction
      diffuserConfig: {
         type: undefined,
         hasBlockout: false,
         blockoutDimensions: ''
      },
       applicationType: 'Wall',
       measurementUnit: undefined,
      applicationDimensions: { x: '', y: '' }
    }
  },
  computed: {
    ...mapGetters(['userInfo']),
    currentConfig() {
      return this.productConfigs[this.selectedProduct] || {}
    },
    currentOptions() {
      return this.currentConfig.options || {}
    },
    generatedSpecCode() {
      const g = this.generator
      const c = this.currentConfig
      if (!c.prefix) return ''
      
      const dim = g.dimension || '___'
      // Only include fields if they are selected/present?
      // For now assume strictly positional.
      return `${c.prefix}-${g.shape || '_'}-${dim}-${g.cct || '_'}-${g.control || '_'}-${g.profile || '_'}-${g.color || '_'}-${g.mounting || '_'}-${c.suffix}`
    },
    currentProvinces() {
       return this.taxRates[this.targetCountry] || {}
    }
  },
  watch: {
    applicationType(val) {
      if (val === 'Ceiling') {
        this.$message.warning('Dimensions: X (longer side), Y (shorter side). X is always the longer dimension for ceiling applications.')
      }
    }
  },
  methods: {
     checkDimensions() {
       const xVal = this.applicationDimensions.x
       const yVal = this.applicationDimensions.y

       // Basic validation: > 0 and no decimals
       if (xVal) {
          if (String(xVal).includes('.')) {
             this.$message.warning('Horizontal dimension cannot contain decimals.')
             return
          }
          if (Number(xVal) <= 0) {
             this.$message.warning('Horizontal dimension must be greater than 0.')
             return
          }
       }
       if (yVal) {
          if (String(yVal).includes('.')) {
             this.$message.warning('Vertical dimension cannot contain decimals.')
             return
          }
          if (Number(yVal) <= 0) {
             this.$message.warning('Vertical dimension must be greater than 0.')
             return
          }
       }

       // Helper for rounding
       const round2 = (num) => (Math.round((Number(num) + Number.EPSILON) * 100) / 100).toFixed(2)

       // Split Calculation Logic
       let xSplit = 0
       let x_len_Split = 0

       if (Number(xVal) > 100) {
          xSplit = Math.floor(Number(xVal) / 100)
          x_len_Split = Number(xVal)/(xSplit+1);

       }else{
          x_len_Split = Number(xVal);
       }


       this.hardwareConfig.splitHorizontalQuantity = xSplit > 0 ? xSplit : 0
       this.hardwareConfig.splitHorizontalLength = x_len_Split > 0 ? round2(x_len_Split) : '0.00'

       let ySplit = 0
       let y_len_Split = 0
       if (Number(yVal) > 100) {
          ySplit = Math.floor(Number(yVal) / 100)
          y_len_Split = Number(yVal)/(ySplit+1)
       } else {
          y_len_Split = Number(yVal)
       }
       this.hardwareConfig.splitVerticalQuantity = ySplit > 0 ? ySplit : 0
       this.hardwareConfig.splitVerticalLength = y_len_Split > 0 ? round2(y_len_Split) : '0.00'

       // Main Crossbeam Logic (< 4) & Clear on Switch & Auto-Calc
       const x = Number(xVal)
       const y = Number(yVal)
       
       // Identify Main Crossbeam (Horizontal if x >= y, else Vertical)
       const newDirection = x >= y ? 'Horizontal' : 'Vertical'
       
       // If direction changed, clear all crossbeam data
       if (this.mainCrossbeamDirection !== newDirection) {
           this.hardwareConfig.crossbeamHorizontalQuantity = ''
           this.hardwareConfig.crossbeamHorizontalLength = ''
           this.hardwareConfig.crossbeamVerticalQuantity = ''
           this.hardwareConfig.crossbeamVerticalLength = ''
           this.mainCrossbeamDirection = newDirection
       }

       if (newDirection === 'Horizontal') {
          // Horizontal is Main
          let mainQty = 0
          if (x < 4) {
             this.hardwareConfig.crossbeamHorizontalQuantity = 0
             this.hardwareConfig.crossbeamHorizontalLength = round2(x)
             this.hardwareConfig.splitHorizontalQuantity = 0
             this.hardwareConfig.splitHorizontalLength = '0.00'
             mainQty = 0
          } else {
             mainQty = Math.ceil(x / 100)
             this.hardwareConfig.crossbeamHorizontalQuantity = mainQty
             this.hardwareConfig.crossbeamHorizontalLength = round2(x)
          }

          // Vertical is Non-Main (Split by Horizontal Main)
          const vBaseQty = Math.ceil(y / 100)
          const divisions = mainQty + 1
          
          this.hardwareConfig.crossbeamVerticalQuantity = vBaseQty * divisions
          this.hardwareConfig.crossbeamVerticalLength = round2(y / divisions)

       } else {
          // Vertical is Main
          let mainQty = 0
          if (y < 4) {
             this.hardwareConfig.crossbeamVerticalQuantity = 0
             this.hardwareConfig.crossbeamVerticalLength = round2(y)
             this.hardwareConfig.splitVerticalQuantity = 0
             this.hardwareConfig.splitVerticalLength = '0.00'
             mainQty = 0
          } else {
             mainQty = Math.ceil(y / 100)
             this.hardwareConfig.crossbeamVerticalQuantity = mainQty
             this.hardwareConfig.crossbeamVerticalLength = round2(y)
          }

          // Horizontal is Non-Main (Split by Vertical Main)
          const hBaseQty = Math.ceil(x / 100)
          const divisions = mainQty + 1
          
          this.hardwareConfig.crossbeamHorizontalQuantity = hBaseQty * divisions
          this.hardwareConfig.crossbeamHorizontalLength = round2(x / divisions)
       }

       if (this.applicationType === 'Ceiling' && xVal && yVal) {
          const x = parseFloat(xVal)
          const y = parseFloat(yVal)
          if (x < y) {
             this.$confirm({
                title: 'Dimension Alert',
                content: 'X is smaller than Y. Do you want to continue?',
                onOk() {},
                onCancel() {}
             })
          }
       }
     },
    add () {
      // Auto-generate Quote No: Q + YYYYMMDD + - + 3 Random Digits
      const date = new Date().toISOString().slice(0, 10).replace(/-/g, '')
      const random = Math.floor(Math.random() * 900) + 100
      const genQuoteNo = `Q${date}-${random}`

      this.edit({ quoteNo: genQuoteNo })
      this.items = []
      this.resetGenerator()
      this.loadDropdownData()
    },
    
    loadDropdownData() {
       // Load Agencies - Try standard pagination params
       const agencyParams = { 
          currentPage: 1, 
          pageSize: 500,
          search: JSON.stringify({}) // Explicit empty search object may be needed by backend parsing
       }
       
       getAction('/agency/list', agencyParams).then(res => {
          console.log('Agency List Response:', res)
          if (res && res.rows) {
             this.agencyList = res.rows
          } else if (res && res.data && res.data.rows) {
             this.agencyList = res.data.rows
          } else if (res && Array.isArray(res)) {
              this.agencyList = res
          }
       }).catch(err => {
           console.error('Failed to load agencies:', err)
       })
    },
    
    loadProjects(agencyName) {
       this.projectList = []
       const params = { pageSize: 500 }
       if (agencyName) {
           params.agency = agencyName
       }
       
       // Load Projects filtered by Agency
       getAction('/lcproject/select', params).then(res => {
          if (res && res.data && res.data.data) {
              this.projectList = res.data.data
          } else if (res && res.data) {
             this.projectList = res.data
          }
       })
    },

    handleAgencyChange(value) {
       const agency = this.agencyList.find(a => a.id === value)
       if (agency) {
          this.form.setFieldsValue({
             customerId: agency.id,
             customerName: agency.name,
             ext4: undefined // Reset project since agency changed
          })
          this.loadProjects(agency.name)
       } else {
          this.projectList = []
       }
    },
    
    // Reset generator defaults whenever product changes or modal opens
    resetGenerator() {
      const defaults = {}
      const opts = this.currentOptions
      
      // Auto-select first option if available
      defaults.shape = opts.shape && opts.shape.length ? opts.shape[0].value : undefined
      defaults.cct = opts.cct && opts.cct.length ? opts.cct[0].value : undefined
      defaults.control = opts.control && opts.control.length ? opts.control[0].value : undefined
      defaults.profile = opts.profile && opts.profile.length ? opts.profile[0].value : undefined
      defaults.color = opts.color && opts.color.length ? opts.color[0].value : undefined
      defaults.mounting = opts.mounting && opts.mounting.length ? opts.mounting[0].value : undefined
      
      defaults.dimension = ''
      
      this.generator = defaults
    },

    handleProductChange() {
       this.resetGenerator()
    },
    
    addFromGenerator() {
      // Allow adding even with empty dimension (will use placeholder)
      this.items.push({
        tempId: Date.now(),
        itemName: this.generatedSpecCode,
        quantity: 1,
        unitPrice: 0
      })
      // Clear dimension after adding to allow next quick entry? Or keep it?
      // Keeping it fits better if they just want to change one attr.
    },
    
    copyCode() {
      const text = this.generatedSpecCode
      if (navigator.clipboard) {
        navigator.clipboard.writeText(text).then(() => {
          this.$message.success('Code copied to clipboard')
        }).catch(() => {
          this.$message.error('Failed to copy')
        })
      } else {
        // Fallback
        const textArea = document.createElement("textarea")
        textArea.value = text
        document.body.appendChild(textArea)
        textArea.select()
        try {
          document.execCommand('copy')
          this.$message.success('Code copied to clipboard')
        } catch (err) {
          this.$message.error('Failed to copy')
        }
        document.body.removeChild(textArea)
      }
    },

    edit (record) {
      this.form.resetFields()
      this.model = Object.assign({}, record)
      this.visible = true
      this.isEdit = !!record.id
      this.items = this.model.items || [] // Should ideally fetch items if edit
      
      this.$nextTick(() => {
        this.form.setFieldsValue({
          quoteNo: this.model.quoteNo,
          agencyId: this.model.customerId, // Map customerId back to Agency Select
          customerId: this.model.customerId,
          customerName: this.model.customerName,
          ext4: this.model.ext4, // Map ext4 back to Project Select
          status: this.model.status || 'Draft',
          currency: this.model.currency || 'CAD',
          totalAmount: this.model.totalAmount
        })
        
        // Restore Location from ext fields if available
        if (this.model.ext1) {
            this.targetCountry = this.model.ext1
        }
        if (this.model.ext2) {
            this.targetProvince = this.model.ext2
            // Trigger tax update
            this.handleProvinceChange()
        } else {
             // Default if new or legacy
            this.targetCountry = 'CA'
            this.targetProvince = 'Ontario'
            this.handleProvinceChange()
        }

        // Infer Tax-Free State
        if (this.model.taxAmount === 0 && this.taxRate > 0 && this.model.totalAmount > 0) {
            this.isTaxFree = true
        } else {
            this.isTaxFree = false
        }
         
        // Ensure dropdowns are loaded if editing directly
        if(this.agencyList.length === 0) {
            this.loadDropdownData()
        }
        
        // Load projects for current agency
        if (this.model.customerName) {
            this.loadProjects(this.model.customerName)
        }
      })
    },
    loadDetail(id) {
       // Since the controller returns the object directly or wrapped, we try getAction
       // Note: Helper implementation might vary, assuming standard getAction usage
       getAction('/lcquote/get/' + id).then(res => {
          let fetchedItems = []
          
          if (res && res.items) {
             fetchedItems = res.items
          } else if (res && res.data && res.data.items) {
             fetchedItems = res.data.items
          } else if (res && res.result && res.result.items) {
             fetchedItems = res.result.items
          }

          if (fetchedItems) {
             // Inject tempId for frontend table stability
             this.items = fetchedItems.map(item => ({
               ...item,
               // Use existing ID as tempId or generate one if missing (shouldn't happen for DB items)
               tempId: item.id || Date.now() + Math.random()
             }))
             this.calculateTotal()
          }
       })
    },
    addItem() {
      this.items.push({
        tempId: Date.now(),
        itemName: '',
        description: '',
        quantity: 1,
        unitPrice: 0
      })
    },
    deleteItem(index) {
      this.items.splice(index, 1)
      this.calculateTotal()
    },
    onQtyChange(record) {
      this.calculateTotal()
    },
    onPriceChange(record) {
       this.calculateTotal()
    },
    handleCountryChange() {
       // Reset province when country changes
       this.targetProvince = undefined 
       this.taxRate = 0
       
       // Auto-set Currency
       const currency = this.targetCountry === 'CA' ? 'CAD' : 'USD'
       console.log('Setting currency to:', currency)
       this.form.setFieldsValue({ currency: currency })

       this.calculateTotal()
    },
    handleProvinceChange() {
       const rates = this.taxRates[this.targetCountry]
       if (rates && this.targetProvince) {
          this.taxRate = rates[this.targetProvince] || 0
       } else {
          this.taxRate = 0
       }
       this.calculateTotal()
    },
    calculateTotal() {
      let total = 0
      this.items.forEach(item => {
        total += (item.quantity * item.unitPrice)
      })
      this.subTotal = total
      
      if (this.isTaxFree) {
        this.taxAmount = 0
      } else {
        this.taxAmount = total * this.taxRate
      }
      
      const grandTotal = total + this.taxAmount
      
      this.form.setFieldsValue({ totalAmount: grandTotal.toFixed(2) })
    },
    close () {
      this.$emit('close')
      this.visible = false
      this.items = []
      this.projectName = undefined
      this.fileList = []
    },
    openDescriptionEditor(record) {
      this.currentEditingItem = record
      this.editorContent = record.description || ''
      this.editorVisible = true
    },
    copySpecsToDescription(record) {
      const c = this.currentConfig
      const g = this.generator
      const opts = this.currentOptions
      
      const specs = []
      
      // Product Family
      if (this.productConfigs[this.selectedProduct]) {
         specs.push(`Product Family: ${this.productConfigs[this.selectedProduct].label}`)
      }

      // Prefix
      if (c.prefix) {
         specs.push(`Prefix: ${c.prefix}`)
      }
      
      const mapping = {
        shape: 'Shape',
        cct: 'CCT',
        control: 'Control',
        profile: 'Profile',
        color: 'Color',
        mounting: 'Mounting'
      }

      // Dimension (Insert after Shape, before others to match visual order roughly)
      // Or just append based on mapping order. Let's handle dimension dynamically or fixed.
      // Based on image: Prefix -> Shape -> Dimension -> CCT -> ...
      
      // Shape
      if (g.shape && opts.shape) {
         const opt = opts.shape.find(o => o.value === g.shape)
         specs.push(`Shape: ${opt ? opt.label : g.shape}`)
      }

      // Dimension
      if (g.dimension) {
         specs.push(`Dimension: ${g.dimension}`)
      }

      // CCT
      if (g.cct && opts.cct) {
         const opt = opts.cct.find(o => o.value === g.cct)
         specs.push(`CCT: ${opt ? opt.label : g.cct}`)
      }

      // Control
      if (g.control && opts.control) {
         const opt = opts.control.find(o => o.value === g.control)
         specs.push(`Control: ${opt ? opt.label : g.control}`)
      }

      // Profile
      if (g.profile && opts.profile) {
         const opt = opts.profile.find(o => o.value === g.profile)
         specs.push(`Profile: ${opt ? opt.label : g.profile}`)
      }

      // Color
      if (g.color && opts.color) {
         const opt = opts.color.find(o => o.value === g.color)
         specs.push(`Color: ${opt ? opt.label : g.color}`)
      }

      // Mounting
      if (g.mounting && opts.mounting) {
         const opt = opts.mounting.find(o => o.value === g.mounting)
         specs.push(`Mounting: ${opt ? opt.label : g.mounting}`)
      }

      // Suffix
      if (c.suffix) {
         specs.push(`Suffix: ${c.suffix}`)
      }
      
      if (specs.length > 0) {
         const text = specs.join('\n')
         let newDesc = record.description || ''
         
         if (newDesc.trim() !== '') {
            newDesc += '\n' + text
         } else {
            newDesc = text
         }
         
         // Use $set to ensure reactivity triggers the UI update immediately
         this.$set(record, 'description', newDesc)
         this.$forceUpdate() // Force re-render to capture state change in table slots if deeply nested
         
         this.$message.success('Specs copied to description')
      } else {
         this.$message.info('No specs selected to copy')
      }
    },
    handleEditorOk() {
        if(this.currentEditingItem) {
            this.currentEditingItem.description = this.editorContent;
        }
        this.editorVisible = false;
        this.editorContent = '';
        this.currentEditingItem = null;
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
            }
        });
    },

    getFileUrl(path) {
        if(!path) return '';
        if(path.startsWith('http')) return path;
        return "/jshERP-boot/systemConfig/static/" + path;
    },
    handleOk () {
      const that = this
      // Validate inputs
      this.form.validateFields((err, values) => {
        console.log('Form Values on Save:', values) 
        if (!err) {
          that.confirmLoading = true
          
          let httpurl = ''
          let method = 'post'
          if (!this.model.id) {
            httpurl = '/lcquote/add'
          } else {
            httpurl = '/lcquote/update'
          }

          // Construct ext4 JSON
          const ext4Data = {
              projectName: this.projectName || '',
              files: this.fileList
          };
          
          let formData = Object.assign(this.model, values, {
             subtotal: this.subTotal,
             taxAmount: this.taxAmount,
             ext5: JSON.stringify(this.items), // Store items in ext5
             ext4: JSON.stringify(ext4Data)
          });
          
          // Force include totalAmount in case it was excluded due to 'disabled'
          formData.totalAmount = this.form.getFieldValue('totalAmount')
          
          // Add Subtotal and Tax Amount
          formData.subtotal = this.subTotal
          formData.taxAmount = this.taxAmount
          
          // Persist Location data using extra fields (ext1, ext2)
          formData.ext1 = this.targetCountry
          formData.ext2 = this.targetProvince
          
          // Persist Operator (ext3)
          let currentOperator = this.form.getFieldValue('ext3')
          if (!currentOperator && this.userInfo) {
             const u = typeof this.userInfo === 'function' ? this.userInfo() : this.userInfo
             if(u && u.username) {
                currentOperator = u.username
             }
          }
          formData.ext3 = currentOperator

          // Filter out tempId and non-backend fields
          formData.items = this.items.map(item => ({
            id: item.id,
            itemName: item.itemName,
            description: item.description,
            quantity: item.quantity,
            unitPrice: item.unitPrice,
            lineTotal: item.lineTotal 
          }))
          
          request({
            url: httpurl,
            method: method,
            data: formData
          }).then((res) => {
            if (res === 200 || res === 1 || res.code === 200) { 
              that.$message.success('Saved successfully')
              that.$emit('ok')
              that.close()
            } else {
               that.$message.warning(res.message || 'Operation failed')
            }
          }).finally(() => {
            that.confirmLoading = false
          })
        }
      })
    },
    handleCancel () {
      this.close()
    }
  }
}
</script>
