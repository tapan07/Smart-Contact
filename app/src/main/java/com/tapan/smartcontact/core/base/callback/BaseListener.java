package com.tapan.smartcontact.core.base.callback;

import com.tapan.smartcontact.core.base.view.BaseView;

public interface BaseListener<V extends BaseView> {

  void onAttach(V view);

  void onDetach();

  void handleError(Throwable error);
}
