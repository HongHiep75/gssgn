import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ResolutionComponent } from './resolution/resolution.component';
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {CommonModule} from "@angular/common";
import {ReactiveFormsModule} from "@angular/forms";
import { ControlMethodComponent } from './control-method/control-method.component';
import { ApplySanctionComponent } from './apply-sanction/apply-sanction.component';
import { ValCollateralFrequencyComponent } from './val-collateral-frequency/val-collateral-frequency.component';
import {NgSelectModule} from "@ng-select/ng-select";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {ProductConditionComponent} from "app/entities/product-condition/product-condition.component";

@NgModule({
  imports: [BrowserAnimationsModule,CommonModule,ReactiveFormsModule,NgSelectModule,
    RouterModule.forChild([
      {
        path: 'area',
        loadChildren: () => import('./area/area.module').then(m => m.GssgnAreaModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
      {
        path: 'resolution',
        component: ResolutionComponent,
      },
      {
        path: 'control-method',
        component: ControlMethodComponent,
      },
      {
        path: 'apply-sanction',
        component: ApplySanctionComponent,
      },
      {
        path: 'val-collateral-frequency',
        component: ValCollateralFrequencyComponent,
      },
      {
        path: 'product-condition',
        component: ProductConditionComponent,
      },
    ]),
    FontAwesomeModule,
  ],
  declarations: [ResolutionComponent, ControlMethodComponent, ApplySanctionComponent
    , ValCollateralFrequencyComponent,ProductConditionComponent],
})
export class GssgnEntityModule {}
