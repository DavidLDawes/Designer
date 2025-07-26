import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { TL } from 'app/entities/enumerations/tl.model';
import { Config } from 'app/entities/enumerations/config.model';
import { ShipsService } from '../service/ships.service';
import { IShips } from '../ships.model';
import { ShipsFormGroup, ShipsFormService } from './ships-form.service';

@Component({
  selector: 'jhi-ships-update',
  templateUrl: './ships-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ShipsUpdateComponent implements OnInit {
  isSaving = false;
  ships: IShips | null = null;
  tLValues = Object.keys(TL);
  configValues = Object.keys(Config);

  usersSharedCollection: IUser[] = [];

  protected shipsService = inject(ShipsService);
  protected shipsFormService = inject(ShipsFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ShipsFormGroup = this.shipsFormService.createShipsFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ships }) => {
      this.ships = ships;
      if (ships) {
        this.updateForm(ships);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ships = this.shipsFormService.getShips(this.editForm);
    if (ships.id !== null) {
      this.subscribeToSaveResponse(this.shipsService.update(ships));
    } else {
      this.subscribeToSaveResponse(this.shipsService.create(ships));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IShips>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(ships: IShips): void {
    this.ships = ships;
    this.shipsFormService.resetForm(this.editForm, ships);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, ships.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.ships?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
