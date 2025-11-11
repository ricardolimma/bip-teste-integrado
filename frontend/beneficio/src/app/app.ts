import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Beneficio } from './models/beneficio';
import { BeneficioService } from './services/beneficio';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.html',
  styleUrls: ['./app.css'],
})
export class AppComponent implements OnInit {
  beneficios: Beneficio[] = [];
  newBeneficio: Beneficio = { nome: '', valor: 0, ativo: true };
  errorMessage: string = '';

  constructor(private beneficioService: BeneficioService) {}

  ngOnInit(): void {
    this.loadBeneficios();
  }

  // LIST
  loadBeneficios() {
    this.beneficioService.getAll().subscribe({
      next: (data) => (this.beneficios = data),
      error: () => (this.errorMessage = 'Erro ao carregar benefícios'),
    });
  }

  // CREATE
  addBeneficio() {
    if (!this.newBeneficio.nome || this.newBeneficio.valor <= 0) {
      this.errorMessage = 'Preencha todos os campos corretamente';
      return;
    }

    this.beneficioService.create(this.newBeneficio).subscribe({
      next: (b) => {
        this.beneficios.push(b);
        this.newBeneficio = { nome: '', valor: 0, ativo: true };
        this.errorMessage = '';
      },
      error: () => (this.errorMessage = 'Erro ao criar benefício'),
    });
  }

  // UPDATE
  updateBeneficio(b: Beneficio) {
    if (!b.id) return;
    this.beneficioService.update(b.id, b).subscribe({
      next: () => this.loadBeneficios(),
      error: () => (this.errorMessage = 'Erro ao atualizar benefício'),
    });
  }

  // DELETE
  deleteBeneficio(id?: number) {
    if (!id) return;
    this.beneficioService.delete(id).subscribe({
      next: () => this.loadBeneficios(),
      error: () => (this.errorMessage = 'Erro ao deletar benefício'),
    });
  }
}
