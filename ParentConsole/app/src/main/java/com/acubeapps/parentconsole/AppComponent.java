package com.acubeapps.parentconsole;

import com.acubeapps.parentconsole.gcm.RegistrationIntentService;

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

    void injectRegistrationIntentService(RegistrationIntentService registrationIntentService);


}
