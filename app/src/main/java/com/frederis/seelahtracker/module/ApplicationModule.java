package com.frederis.seelahtracker.module;

import com.frederis.seelahtracker.CardManager;
import com.frederis.seelahtracker.MainActivity;
import com.frederis.seelahtracker.widget.DeckView;
import com.frederis.seelahtracker.widget.HandView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(injects = {MainActivity.class, DeckView.class, HandView.class}, library = true)
public class ApplicationModule {

    @Provides
    @Singleton
    public CardManager provideCardManager() {
        return new CardManager();
    }

}
