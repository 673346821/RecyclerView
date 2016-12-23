# RecyclerView
![image](https://github.com/673346821/RecyclerView/blob/master/src/main/assets/20161223162556134.gif)

![image](https://github.com/673346821/RecyclerView/blob/master/src/main/assets/ccc.gif)

![image](https://github.com/673346821/RecyclerView/blob/master/src/main/assets/sdfsdf.gif)


只需要在activity_recycleview.xml中进行配置即可：

```
 <enum name="LISTVIEW_VERTICAL" value="0" />
            <enum name="LISTVIEW_HORIZONTAL" value="1" />
            <enum name="GRIDVIEW_NORMAL" value="2" />
            <enum name="STAGGERED_HORIZONTAL" value="3" />
            <enum name="STAGGERED_VERTICAL" value="4" />
```

```
  <com.safly.recyclerview.FinalRecycleView
        android:id="@+id/finalRecycleView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:gridSpanCount="2"
        app:innerDivider="@color/divider"
        app:innerDividerWidth="1dp"
        app:showStyle="LISTVIEW_HORIZONTAL" />
```

