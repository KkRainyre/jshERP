---
description: Customizing the Project Detail Page UI
---

This workflow describes how to customize the User Interface of the Project Detail page (`ProjectDetail.vue`).

## 1. Locate the Component
The main file for the Project Detail view is located at:
`src/views/system/ProjectDetail.vue`

## 2. Modifying the Sidebar Profile
The left sidebar contains the project profile information.

### Removing the Avatar
To remove the avatar, locate the `.profile-header` section and delete the `.avatar-wrapper` div:
```html
<!-- REMOVED -->
<!--
<div class="avatar-wrapper">
  <a-avatar ...> ... </a-avatar>
</div>
-->
```

### Adjusting Text Styles
To change the size of the Project Name:
1. Scroll down to the `<style>` section.
2. Find the `.profile-info .name` class.
3. Update the `font-size` property:
```css
.profile-info {
  .name {
    font-size: 24px; /* Updated from 18px */
    font-weight: 600;
    /* ... */
  }
}
```

## 3. Customizing the Action Button
The "Actions" button in the header toggles the right sidebar visibility.
The logic is handled by:
- `showActionsMenu` data property
- `toggleActionsMenu()` method
- `v-show="showActionsMenu"` on the right column `<a-col>`

## 4. Center Content Layout
The center content column width is dynamic using the ternary operator on `span` props:
```html
<a-col :md="showActionsMenu ? 10 : 17" ...>
```
If you wish to change the layout behavior, adjust these values.
