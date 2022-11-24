import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { GssgnSharedModule } from 'app/shared/shared.module';
import { GssgnCoreModule } from 'app/core/core.module';
import { GssgnAppRoutingModule } from './app-routing.module';
import { GssgnHomeModule } from './home/home.module';
import { GssgnEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';
import {ResolutionComponent} from "app/entities/resolution/resolution.component";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatButtonModule} from "@angular/material/button";

@NgModule({
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    GssgnSharedModule,
    GssgnCoreModule,
    GssgnHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    GssgnEntityModule,
    GssgnAppRoutingModule,
    MatButtonModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent
    , PageRibbonComponent, ActiveMenuDirective, FooterComponent],
  bootstrap: [MainComponent],
})
export class GssgnAppModule {}
