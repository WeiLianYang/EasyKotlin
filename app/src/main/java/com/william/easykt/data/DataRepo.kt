/*
 * Copyright WeiLianYang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.william.easykt.data

import java.io.Serializable

/**
 * @author William
 * @date 2020/5/19 13:05
 * Class Comment：业务数据返回模型
 */

// 通用的带有列表数据的实体
data class BaseListResponseBody<T>(
    val curPage: Int,
    val datas: List<T>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)

//文章
data class ArticleResponseBody(
    val curPage: Int,
    var datas: MutableList<Article>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)

//文章
data class Article(
    val apkLink: String,
    val audit: Int,
    val author: String,
    val chapterId: Int,
    val chapterName: String,
    var collect: Boolean,
    val courseId: Int,
    val desc: String,
    val envelopePic: String,
    val fresh: Boolean,
    val id: Int,
    val link: String,
    val niceDate: String,
    val niceShareDate: String,
    val origin: String,
    val prefix: String,
    val projectLink: String,
    val publishTime: Long,
    val shareDate: String,
    val shareUser: String,
    val superChapterId: Int,
    val superChapterName: String,
    val tags: MutableList<Tag>,
    val title: String,
    val type: Int,
    val userId: Int,
    val visible: Int,
    val zan: Int,
    var top: String
)

data class Tag(
    val name: String,
    val url: String
)

//轮播图
data class Banner(
    val desc: String,
    val id: Int,
    val imagePath: String,
    val isVisible: Int,
    val order: Int,
    val title: String,
    val type: Int,
    val url: String
)

data class HotKey(
    val id: Int,
    val link: String,
    val name: String,
    val order: Int,
    val visible: Int
)

//常用网站
data class Friend(
    val icon: String,
    val id: Int,
    val link: String,
    val name: String,
    val order: Int,
    val visible: Int
)

//知识体系
data class KnowledgeTreeBody(
    val children: MutableList<Knowledge>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val visible: Int
) : Serializable

data class Knowledge(
    val children: List<Any>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val visible: Int
) : Serializable

// 登录数据
data class LoginData(
    val chapterTops: MutableList<String>,
    val collectIds: MutableList<String>,
    val email: String,
    val icon: String,
    val id: Int,
    val password: String,
    val token: String,
    val type: Int,
    val username: String
)

//收藏网站
data class CollectionWebsite(
    val desc: String,
    val icon: String,
    val id: Int,
    var link: String,
    var name: String,
    val order: Int,
    val userId: Int,
    val visible: Int
)

data class CollectionArticle(
    val author: String,
    val chapterId: Int,
    val chapterName: String,
    val courseId: Int,
    val desc: String,
    val envelopePic: String,
    val id: Int,
    val link: String,
    val niceDate: String,
    val origin: String,
    val originId: Int,
    val publishTime: Long,
    val title: String,
    val userId: Int,
    val visible: Int,
    val zan: Int
)

// 导航
data class NavigationBean(
    val articles: MutableList<Article>,
    val cid: Int,
    val name: String
)

// 项目
data class ProjectTreeBean(
    val children: List<Any>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val visible: Int
)

// 热门搜索
data class HotSearchBean(
    val id: Int,
    val link: String,
    val name: String,
    val order: Int,
    val visible: Int
)


// TODO工具 类型
data class TodoTypeBean(
    val type: Int,
    val name: String,
    var isSelected: Boolean
)

// TODO实体类
data class TodoBean(
    val id: Int,
    val completeDate: String,
    val completeDateStr: String,
    val content: String,
    val date: Long,
    val dateStr: String,
    val status: Int,
    val title: String,
    val type: Int,
    val userId: Int,
    val priority: Int
) : Serializable

data class TodoListBean(
    val date: Long,
    val todoList: MutableList<TodoBean>
)

// 所有TODO，包括待办和已完成
data class AllTodoResponseBody(
    val type: Int,
    val doneList: MutableList<TodoListBean>,
    val todoList: MutableList<TodoListBean>
)

data class TodoResponseBody(
    val curPage: Int,
    val datas: MutableList<TodoBean>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)

// 公众号列表实体
data class WXChapterBean(
    val children: MutableList<String>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
)

// 用户个人信息
data class UserInfoBody(
    val coinCount: Int, // 总积分
    val rank: Int, // 当前排名
    val userId: Int,
    val username: String
)

// 个人积分实体
data class UserScoreBean(
    val coinCount: Int,
    val date: Long,
    val desc: String,
    val id: Int,
    val reason: String,
    val type: Int,
    val userId: Int,
    val userName: String
)

// 排行榜实体
data class CoinInfoBean(
    val coinCount: Int,
    val level: Int,
    val rank: Int,
    val userId: Int,
    val username: String
)

// 我的分享
data class ShareResponseBody(
    val coinInfo: CoinInfoBean,
    val shareArticles: ArticleResponseBody
)

class MainEntranceBean(
    var title: Int = 0
)

class FlowUsageBean(
    var title: Int = 0
)
