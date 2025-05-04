<script setup>
import { ref, onMounted } from 'vue'
import { apiQueryCollectionTopicList } from '@/api/topic/topic'
import dyajs from 'dayjs'

const favoriteList = ref([
])

const getFavoriteList = () => {
  apiQueryCollectionTopicList().then(res => {
    favoriteList.value = res.data
  })
}

onMounted(() => {
  getFavoriteList()
})
</script>

<template>
  <view class="favorite">
    <view class="favorite-container">
      <view class="list-wrapper">
        <view class="list-item" v-for="item in favoriteList" :key="item.id">
          <view class="item-content">
            <view class="item-right">
              <text class="title">{{ item.topicName }}</text>
              <uni-icons type="star-filled" size="20" color="#1677ff"></uni-icons>
            </view>
            <view class="info-row">
              <view class="tags-row">
                <view class="tag" v-for="(tag, index) in item.labelNames" :key="index">
                  {{ tag }}
                </view>
              </view>
              <view class="time-box">
                <text class="time">{{ dyajs(item.collectionTime).format(
                  'YYYY-MM-DD HH:mm:ss'
                ) }}</text>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<style lang="scss" scoped>
.favorite {
  min-height: 100vh;
  background: #f5f7fa;
  padding: 20rpx;

  .favorite-container {
    .list-wrapper {
      .list-item {
        background: #fff;
        border-radius: 16rpx;
        padding: 30rpx 30rpx 15rpx 30rpx;
        margin-bottom: 15rpx;
        display: flex;
        align-items: flex-start;
        justify-content: space-between;
        box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
        transition: all 0.3s ease;
        border-left: 8rpx solid #1677ff;

        &:active {
          transform: scale(0.98);
        }

        .item-content {
          flex: 1;

          .item-right {
            display: flex;
            justify-content: space-between;
          }

          .info-row {
            padding-top: 15rpx;
            display: flex;
            justify-content: space-between;
            align-items: center;

            .tags-row {
              display: flex;
              flex-wrap: wrap;
              gap: 12rpx;

              .tag {
                padding: 6rpx 20rpx;
                background: rgba(22, 119, 255, 0.08);
                color: #1677ff;
                font-size: 24rpx;
                border-radius: 24rpx;
                font-weight: 500;
              }
            }

            .time-box {
              min-width: 200rpx;
              text-align: right;

              .time {
                font-size: 24rpx;
                color: #999;
              }
            }
          }
        }
      }
    }
  }
}
</style>