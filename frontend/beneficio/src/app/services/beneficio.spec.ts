import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { BeneficioService } from './beneficio';
import { Beneficio } from '../models/beneficio';

describe('BeneficioService', () => {
  let service: BeneficioService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [BeneficioService]
    });
    service = TestBed.inject(BeneficioService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should fetch all beneficios', () => {
    const dummyBeneficios: Beneficio[] = [
      { id: 1, nome: 'Vale Alimentação', valor: 100, ativo: true },
      { id: 2, nome: 'Vale Transporte', valor: 50, ativo: true }
    ];

    service.getAll().subscribe(beneficios => {
      expect(beneficios.length).toBe(2);
      expect(beneficios).toEqual(dummyBeneficios);
    });

    const req = httpMock.expectOne('/api/beneficios');
    expect(req.request.method).toBe('GET');
    req.flush(dummyBeneficios);
  });

  it('should create a beneficio', () => {
    const newBeneficio: Beneficio = { nome: 'Plano Saúde', valor: 200, ativo: true };

    service.create(newBeneficio).subscribe(beneficio => {
      expect(beneficio).toEqual({ ...newBeneficio, id: 3 });
    });

    const req = httpMock.expectOne('/api/beneficios');
    expect(req.request.method).toBe('POST');
    req.flush({ ...newBeneficio, id: 3 });
  });
});
