package cn.com.jinwang.components.datable;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigationToolbar;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;

import cn.com.jinwang.components.pure.PurePagingNavigator;

public class JwNavigationToolbar extends NavigationToolbar {

  private static final long serialVersionUID = 1L;
  
  public JwNavigationToolbar(DataTable<?, ?> table) {
    super(table);
  }

  @Override
  protected PagingNavigator newPagingNavigator(String navigatorId, DataTable<?, ?> table) {
    return new PurePagingNavigator(navigatorId, table);
  }

}
