import { TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { AppComponent } from './app';
import { BeneficioService } from './services/beneficio';
import { Beneficio } from './models/beneficio';

describe('AppComponent', () => {
  let component: AppComponent;
  let service: BeneficioService;

  // Mock do serviço usando Jasmine
  const mockService = {
    getAll: jasmine.createSpy('getAll'),
    create: jasmine.createSpy('create'),
    update: jasmine.createSpy('update'),
    delete: jasmine.createSpy('delete')
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        AppComponent,
        { provide: BeneficioService, useValue: mockService }
      ]
    });

    component = TestBed.inject(AppComponent);
    service = TestBed.inject(BeneficioService);
  });

  afterEach(() => {
    mockService.getAll.calls.reset();
    mockService.create.calls.reset();
    mockService.update.calls.reset();
    mockService.delete.calls.reset();
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  describe('loadBeneficios', () => {
    it('should load beneficios successfully', () => {
      const beneficiosMock: Beneficio[] = [
        { id: 1, nome: 'Beneficio 1', valor: 100, ativo: true },
        { id: 2, nome: 'Beneficio 2', valor: 200, ativo: false }
      ];
      mockService.getAll.and.returnValue(of(beneficiosMock));

      component.loadBeneficios();

      expect(component.beneficios).toEqual(beneficiosMock);
      expect(component.errorMessage).toBe('');
    });

    it('should set errorMessage on failure', () => {
      mockService.getAll.and.returnValue(throwError(() => new Error('fail')));

      component.loadBeneficios();

      expect(component.beneficios).toEqual([]);
      expect(component.errorMessage).toBe('Erro ao carregar benefícios');
    });
  });

  describe('addBeneficio', () => {
    it('should add new beneficio successfully', () => {
      const newB: Beneficio = { nome: 'Novo', valor: 150, ativo: true };
      mockService.create.and.returnValue(of({ ...newB, id: 1 }));

      component.newBeneficio = { ...newB };
      component.addBeneficio();

      expect(component.beneficios.length).toBe(1);
      expect(component.beneficios[0]).toEqual({ ...newB, id: 1 });
      expect(component.newBeneficio.nome).toBe('');
      expect(component.errorMessage).toBe('');
    });

    it('should set errorMessage if fields are invalid', () => {
      component.newBeneficio = { nome: '', valor: 0, ativo: true };
      component.addBeneficio();

      expect(component.errorMessage).toBe('Preencha todos os campos corretamente');
      expect(component.beneficios.length).toBe(0);
    });

    it('should handle create service error', () => {
      component.newBeneficio = { nome: 'Erro', valor: 100, ativo: true };
      mockService.create.and.returnValue(throwError(() => new Error('fail')));

      component.addBeneficio();

      expect(component.errorMessage).toBe('Erro ao criar benefício');
      expect(component.beneficios.length).toBe(0);
    });
  });

  describe('updateBeneficio', () => {
    it('should update beneficio successfully', () => {
      const b: Beneficio = { id: 1, nome: 'Atualizado', valor: 200, ativo: true };
      mockService.update.and.returnValue(of({ ...b }));

      component.updateBeneficio(b);

      expect(mockService.update).toHaveBeenCalledWith(1, b);
      expect(component.errorMessage).toBe('');
    });

    it('should handle update error', () => {
      const b: Beneficio = { id: 1, nome: 'Erro', valor: 100, ativo: true };
      mockService.update.and.returnValue(throwError(() => new Error('fail')));

      component.updateBeneficio(b);

      expect(component.errorMessage).toBe('Erro ao atualizar benefício');
    });
  });

  describe('deleteBeneficio', () => {
    it('should delete beneficio successfully', () => {
      mockService.delete.and.returnValue(of(undefined));

      component.deleteBeneficio(1);

      expect(mockService.delete).toHaveBeenCalledWith(1);
      expect(component.errorMessage).toBe('');
    });

    it('should handle delete error', () => {
      mockService.delete.and.returnValue(throwError(() => new Error('fail')));

      component.deleteBeneficio(1);

      expect(component.errorMessage).toBe('Erro ao deletar benefício');
    });
  });
});
