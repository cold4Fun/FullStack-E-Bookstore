<style scoped>
.no-order {
  text-align: center;
  margin: 30px 0;
}

.no-order img {
  margin: auto;
  display: block;
  height: 210px;
}

.no-order div {
  font-size: 30px;
  font-weight: bold;
  color: #57575c;
}

.order {
  border: 1px solid #d7d7d7;
  background-color: #ffffff;
  margin-bottom: 31px;
}

.order-title {
  height: 0;
  line-height: 55px;
  background-color: #ffffff;
  padding: 0 43px;
  font-size: 16px;
  color: #666666;
}

.item-info span {
  margin: 0 9px;
}

.money {
  margin-left: 5px;
  font-size: 24px;
  color: #505050;
}

.order-content {
  padding: 0 43px;
}

.good-box {
  width: 88%;
}

.good-list {
  display: flex;
  align-items: center;
  height: 145px;
}

.good-img {
  width: 80px;
}

.good-img img {
  width: 100%;
}

.good-name {
  margin-left: 30px;
  font-size: 16px;
  color: #505050;
}

.good-name .p-name {
  margin-bottom: 5px;
}

.p-name {
  font-weight: bold;
}

.good-total {
  height: 0;
  line-height: 145px;
  font-size: 18px;
  font-weight: 300;
  color: #007bff;
}

.float-l {
  float: left;
}

.float-r {
  float: right;
}

.clearFix:before,
.clearFix:after {
  content: " ";
  display: table;
}

.clearFix:after {
  clear: both;
}
</style>

<template>
  <div class="no-order" v-if="!orderDetailsStore.hasOrderDetails()">
    <p>Sorry, the order you requested cannot be found!</p>
    <div>You have not placed any order</div>
    <router-link :to="'../category/' + categoryStore.selectedCategoryName">
      <button class="button">Continue Shopping!</button>
    </router-link>
  </div>
  <div class="order" v-else>
    <div class="order-title">
      <div class="item-info float-l">
        {{ DateToLocalDate(new Date(orderDetails.order.dateCreated)) }}
        <span>|</span>
        Confirmation #: {{ orderDetails.order.confirmationNumber }}
        <span>|</span>
        {{ orderDetails.customer.customerName }}
        <span>|</span>
        {{ orderDetails.customer.address }}
      </div>
      <div class="item-info float-l">
        <span>|</span>
        {{ orderDetails.customer.email }}
        <span>|</span>
        {{ orderDetails.customer.phone }}
        <span>|</span>
        **** **** ****
        {{ orderDetails.customer.ccNumber.slice(12, 16) }} ({{
          new Date(orderDetails.customer.ccExpDate).getMonth() + 1
        }}/{{ new Date(orderDetails.customer.ccExpDate).getFullYear() }})
        <span>|</span>
        <span class="p-name">Surcharge: ${{ (500 / 100).toFixed(2) }}</span>
      </div>
      <div class="item-money float-r">
        <span>Total:</span>
        <span class="money"
          >${{ (orderDetails.order.amount / 100).toFixed(2) }}</span
        >
      </div>
    </div>
    <div class="order-content clearFix">
      <div class="good-box float-l">
        <div
          class="good-list"
          v-for="(bookItem, i) in orderDetails.books"
          v-bind:key="i"
        >
          <div class="good-img">
            <img
              :src="
                require('@/assets/images/books/' + bookImageFileName(bookItem))
              "
              :alt="bookItem.title"
              width="100px"
              height="auto"
            />
          </div>
          <div class="good-name">
            <div class="p-name">{{ bookItem.title }}</div>
            <div class="p-money">
              [${{ (bookItem.price / 100).toFixed(2) }}] x
              {{ orderDetails.lineItems[i].quantity }}
            </div>
          </div>
        </div>
      </div>
      <div class="good-total float-r">
        <div
          class="good-list"
          v-for="(bookItem, i) in orderDetails.books"
          v-bind:key="i"
        >
          <span
            >${{
              (
                (bookItem.price * orderDetails.lineItems[i].quantity) /
                100
              ).toFixed(2)
            }}</span
          >
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useOrderDetailsStore } from "@/stores/OrderDetailsStore";
import { computed } from "vue";
import { BookItem } from "@/types";

const orderDetailsStore = useOrderDetailsStore();
const orderDetails = computed(() => {
  return orderDetailsStore.orderDetails;
});

import { useCartStore } from "@/stores/CartStore";
const cartStore = useCartStore();
import { useCategoryStore } from "@/stores/CategoryStore";
const categoryStore = useCategoryStore();

const bookImageFileName = function (book: BookItem): string {
  let name = book.title.toLowerCase();
  name = name.replace(/ /g, "-");
  name = name.replace(/'/g, "");
  return `${name}.jpg`;
};

function DateToLocalDate(date: Date) {
  return new Intl.DateTimeFormat("default", {
    year: "numeric",
    month: "numeric",
    day: "numeric",
    hour: "numeric",
    minute: "numeric",
    second: "numeric",
  }).format(date);
}
</script>
