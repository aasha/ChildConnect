package com.acubeapps.parentconsole;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by aasha.medhi on 9/10/16.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void injectMainApplication(MainApplication application);

    void injectSignInActivity(SignInActivity activity);

    void injectChildListActivity(ChildListActivity activity);

    void injectChildDetailsActivity(ChildDetailsActivity activity);

    void injectAppUsageActivity(AppUsageActivity activity);

    void injectBrowserHistoryActivity(BrowserHistoryActivity activity);

    void injectUpdatePolicyActivity(UpdatePolicyActivity activity);

    void injectPerformaceActivity(PerformanceActivity activity);


}
