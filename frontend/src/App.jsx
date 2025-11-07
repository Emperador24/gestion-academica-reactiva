import React, { useState, useEffect } from 'react';
import { AlertCircle, Plus, Edit2, Trash2, Users, BookOpen, GraduationCap, TrendingUp } from 'lucide-react';

const API_URL = 'http://localhost:8080/api';

const App = () => {
  const [activeTab, setActiveTab] = useState('materias');
  const [materias, setMaterias] = useState([]);
  const [estudiantes, setEstudiantes] = useState([]);
  const [notas, setNotas] = useState([]);
  const [selectedMateria, setSelectedMateria] = useState(null);
  const [selectedEstudiante, setSelectedEstudiante] = useState(null);
  const [notaAcumulada, setNotaAcumulada] = useState(null);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);

  const [materiaForm, setMateriaForm] = useState({ nombre: '', creditos: '' });
  const [estudianteForm, setEstudianteForm] = useState({ nombre: '', apellido: '', email: '', codigo: '' });
  const [notaForm, setNotaForm] = useState({ valor: '', porcentaje: '', descripcion: '', estudianteMateriaId: '' });
  const [inscripcionForm, setInscripcionForm] = useState({ estudianteId: '', materiaId: '' });

  const [editingMateria, setEditingMateria] = useState(null);
  const [editingEstudiante, setEditingEstudiante] = useState(null);
  const [editingNota, setEditingNota] = useState(null);

  useEffect(() => {
    cargarMaterias();
    cargarEstudiantes();
  }, []);

  const showError = (msg) => {
    setError(msg);
    setTimeout(() => setError(''), 5000);
  };

  const showSuccess = (msg) => {
    setSuccess(msg);
    setTimeout(() => setSuccess(''), 3000);
  };

  const cargarMaterias = async () => {
    try {
      const response = await fetch(`${API_URL}/materias`);
      const data = await response.json();
      setMaterias(data);
    } catch (err) {
      showError('Error al cargar materias');
    }
  };

  const cargarEstudiantes = async () => {
    try {
      const response = await fetch(`${API_URL}/estudiantes`);
      const data = await response.json();
      setEstudiantes(data);
    } catch (err) {
      showError('Error al cargar estudiantes');
    }
  };

  const crearMateria = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const response = await fetch(`${API_URL}/materias`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ ...materiaForm, creditos: parseInt(materiaForm.creditos) })
      });
      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message);
      }
      showSuccess('Materia creada exitosamente');
      setMateriaForm({ nombre: '', creditos: '' });
      cargarMaterias();
    } catch (err) {
      showError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const actualizarMateria = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const response = await fetch(`${API_URL}/materias/${editingMateria.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ ...materiaForm, creditos: parseInt(materiaForm.creditos) })
      });
      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message);
      }
      showSuccess('Materia actualizada exitosamente');
      setMateriaForm({ nombre: '', creditos: '' });
      setEditingMateria(null);
      cargarMaterias();
    } catch (err) {
      showError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const eliminarMateria = async (id) => {
    if (!confirm('¿Está seguro de eliminar esta materia?')) return;
    try {
      await fetch(`${API_URL}/materias/${id}`, { method: 'DELETE' });
      showSuccess('Materia eliminada exitosamente');
      cargarMaterias();
    } catch (err) {
      showError('Error al eliminar materia');
    }
  };

  const crearEstudiante = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const response = await fetch(`${API_URL}/estudiantes`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(estudianteForm)
      });
      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message);
      }
      showSuccess('Estudiante creado exitosamente');
      setEstudianteForm({ nombre: '', apellido: '', email: '', codigo: '' });
      cargarEstudiantes();
    } catch (err) {
      showError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const actualizarEstudiante = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const response = await fetch(`${API_URL}/estudiantes/${editingEstudiante.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(estudianteForm)
      });
      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message);
      }
      showSuccess('Estudiante actualizado exitosamente');
      setEstudianteForm({ nombre: '', apellido: '', email: '', codigo: '' });
      setEditingEstudiante(null);
      cargarEstudiantes();
    } catch (err) {
      showError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const eliminarEstudiante = async (id) => {
    if (!confirm('¿Está seguro de eliminar este estudiante?')) return;
    try {
      await fetch(`${API_URL}/estudiantes/${id}`, { method: 'DELETE' });
      showSuccess('Estudiante eliminado exitosamente');
      cargarEstudiantes();
    } catch (err) {
      showError('Error al eliminar estudiante');
    }
  };

  const inscribirEstudiante = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const response = await fetch(`${API_URL}/inscripciones`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          estudianteId: parseInt(inscripcionForm.estudianteId),
          materiaId: parseInt(inscripcionForm.materiaId)
        })
      });
      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message);
      }
      showSuccess('Estudiante inscrito exitosamente');
      setInscripcionForm({ estudianteId: '', materiaId: '' });
    } catch (err) {
      showError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const verEstudiantesMateria = async (materiaId) => {
    try {
      const response = await fetch(`${API_URL}/materias/${materiaId}/estudiantes`);
      const data = await response.json();
      setSelectedMateria(data);
    } catch (err) {
      showError('Error al cargar estudiantes de la materia');
    }
  };

  const verNotasEstudiante = async (estudianteId, materiaId) => {
    try {
      const response = await fetch(`${API_URL}/notas/estudiante/${estudianteId}/materia/${materiaId}/acumulada`);
      const data = await response.json();
      setNotaAcumulada(data);
      setSelectedEstudiante({ estudianteId, materiaId });
    } catch (err) {
      showError('Error al cargar notas del estudiante');
    }
  };

  const crearNota = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const response = await fetch(`${API_URL}/notas`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          ...notaForm,
          valor: parseFloat(notaForm.valor),
          porcentaje: parseInt(notaForm.porcentaje),
          estudianteMateriaId: parseInt(notaForm.estudianteMateriaId)
        })
      });
      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message);
      }
      showSuccess('Nota creada exitosamente');
      setNotaForm({ valor: '', porcentaje: '', descripcion: '', estudianteMateriaId: '' });
      if (selectedEstudiante) {
        verNotasEstudiante(selectedEstudiante.estudianteId, selectedEstudiante.materiaId);
      }
    } catch (err) {
      showError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const eliminarNota = async (id) => {
    if (!confirm('¿Está seguro de eliminar esta nota?')) return;
    try {
      await fetch(`${API_URL}/notas/${id}`, { method: 'DELETE' });
      showSuccess('Nota eliminada exitosamente');
      if (selectedEstudiante) {
        verNotasEstudiante(selectedEstudiante.estudianteId, selectedEstudiante.materiaId);
      }
    } catch (err) {
      showError('Error al eliminar nota');
    }
  };

  const editarMateria = (materia) => {
    setEditingMateria(materia);
    setMateriaForm({ nombre: materia.nombre, creditos: materia.creditos.toString() });
  };

  const editarEstudiante = (estudiante) => {
    setEditingEstudiante(estudiante);
    setEstudianteForm({
      nombre: estudiante.nombre,
      apellido: estudiante.apellido,
      email: estudiante.email,
      codigo: estudiante.codigo
    });
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="bg-blue-600 text-white p-6 shadow-lg">
        <div className="max-w-7xl mx-auto">
          <h1 className="text-3xl font-bold flex items-center gap-2">
            <GraduationCap size={32} />
            Sistema de Gestión Académica Reactiva
          </h1>
          <p className="text-blue-100 mt-2">Universidad Javeriana - Spring WebFlux</p>
        </div>
      </div>

      {error && (
        <div className="max-w-7xl mx-auto mt-4 px-4">
          <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded flex items-center gap-2">
            <AlertCircle size={20} />
            {error}
          </div>
        </div>
      )}

      {success && (
        <div className="max-w-7xl mx-auto mt-4 px-4">
          <div className="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded">
            {success}
          </div>
        </div>
      )}

      <div className="max-w-7xl mx-auto p-6">
        <div className="flex gap-2 mb-6 border-b">
          {['materias', 'estudiantes', 'inscripciones', 'notas'].map(tab => (
            <button
              key={tab}
              onClick={() => setActiveTab(tab)}
              className={`px-6 py-3 font-medium capitalize transition-colors ${
                activeTab === tab
                  ? 'border-b-2 border-blue-600 text-blue-600'
                  : 'text-gray-600 hover:text-blue-600'
              }`}
            >
              {tab}
            </button>
          ))}
        </div>

        {activeTab === 'materias' && (
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            <div className="bg-white p-6 rounded-lg shadow">
              <h2 className="text-xl font-bold mb-4 flex items-center gap-2">
                <BookOpen size={24} className="text-blue-600" />
                {editingMateria ? 'Editar Materia' : 'Nueva Materia'}
              </h2>
              <form onSubmit={editingMateria ? actualizarMateria : crearMateria}>
                <div className="mb-4">
                  <label className="block text-sm font-medium mb-2">Nombre</label>
                  <input
                    type="text"
                    value={materiaForm.nombre}
                    onChange={(e) => setMateriaForm({...materiaForm, nombre: e.target.value})}
                    className="w-full px-3 py-2 border rounded focus:ring-2 focus:ring-blue-500 outline-none"
                    required
                  />
                </div>
                <div className="mb-4">
                  <label className="block text-sm font-medium mb-2">Créditos</label>
                  <input
                    type="number"
                    min="1"
                    value={materiaForm.creditos}
                    onChange={(e) => setMateriaForm({...materiaForm, creditos: e.target.value})}
                    className="w-full px-3 py-2 border rounded focus:ring-2 focus:ring-blue-500 outline-none"
                    required
                  />
                </div>
                <div className="flex gap-2">
                  <button
                    type="submit"
                    disabled={loading}
                    className="flex-1 bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 disabled:opacity-50"
                  >
                    {editingMateria ? 'Actualizar' : 'Crear'}
                  </button>
                  {editingMateria && (
                    <button
                      type="button"
                      onClick={() => {
                        setEditingMateria(null);
                        setMateriaForm({ nombre: '', creditos: '' });
                      }}
                      className="px-4 py-2 border rounded hover:bg-gray-100"
                    >
                      Cancelar
                    </button>
                  )}
                </div>
              </form>
            </div>

            <div className="bg-white p-6 rounded-lg shadow">
              <h2 className="text-xl font-bold mb-4">Lista de Materias</h2>
              <div className="space-y-2 max-h-96 overflow-y-auto">
                {materias.map(materia => (
                  <div key={materia.id} className="p-4 border rounded hover:bg-gray-50">
                    <div className="flex justify-between items-start">
                      <div className="flex-1">
                        <h3 className="font-semibold">{materia.nombre}</h3>
                        <p className="text-sm text-gray-600">Créditos: {materia.creditos}</p>
                      </div>
                      <div className="flex gap-2">
                        <button
                          onClick={() => verEstudiantesMateria(materia.id)}
                          className="p-2 text-blue-600 hover:bg-blue-50 rounded"
                          title="Ver estudiantes"
                        >
                          <Users size={18} />
                        </button>
                        <button
                          onClick={() => editarMateria(materia)}
                          className="p-2 text-yellow-600 hover:bg-yellow-50 rounded"
                        >
                          <Edit2 size={18} />
                        </button>
                        <button
                          onClick={() => eliminarMateria(materia.id)}
                          className="p-2 text-red-600 hover:bg-red-50 rounded"
                        >
                          <Trash2 size={18} />
                        </button>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </div>

            {selectedMateria && (
              <div className="lg:col-span-2 bg-white p-6 rounded-lg shadow">
                <div className="flex justify-between items-center mb-4">
                  <h2 className="text-xl font-bold">Estudiantes en: {selectedMateria.nombre}</h2>
                  <button
                    onClick={() => setSelectedMateria(null)}
                    className="text-gray-600 hover:text-gray-800"
                  >
                    Cerrar
                  </button>
                </div>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                  {selectedMateria.estudiantes?.map(estudiante => (
                    <div key={estudiante.id} className="p-3 border rounded">
                      <p className="font-semibold">{estudiante.nombre} {estudiante.apellido}</p>
                      <p className="text-sm text-gray-600">{estudiante.email}</p>
                      <p className="text-sm text-gray-600">Código: {estudiante.codigo}</p>
                      <button
                        onClick={() => verNotasEstudiante(estudiante.id, selectedMateria.id)}
                        className="mt-2 text-sm text-blue-600 hover:underline flex items-center gap-1"
                      >
                        <TrendingUp size={14} />
                        Ver notas
                      </button>
                    </div>
                  ))}
                </div>
              </div>
            )}
          </div>
        )}

        {activeTab === 'estudiantes' && (
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            <div className="bg-white p-6 rounded-lg shadow">
              <h2 className="text-xl font-bold mb-4 flex items-center gap-2">
                <Users size={24} className="text-blue-600" />
                {editingEstudiante ? 'Editar Estudiante' : 'Nuevo Estudiante'}
              </h2>
              <form onSubmit={editingEstudiante ? actualizarEstudiante : crearEstudiante}>
                <div className="mb-4">
                  <label className="block text-sm font-medium mb-2">Nombre</label>
                  <input
                    type="text"
                    value={estudianteForm.nombre}
                    onChange={(e) => setEstudianteForm({...estudianteForm, nombre: e.target.value})}
                    className="w-full px-3 py-2 border rounded focus:ring-2 focus:ring-blue-500 outline-none"
                    required
                  />
                </div>
                <div className="mb-4">
                  <label className="block text-sm font-medium mb-2">Apellido</label>
                  <input
                    type="text"
                    value={estudianteForm.apellido}
                    onChange={(e) => setEstudianteForm({...estudianteForm, apellido: e.target.value})}
                    className="w-full px-3 py-2 border rounded focus:ring-2 focus:ring-blue-500 outline-none"
                    required
                  />
                </div>
                <div className="mb-4">
                  <label className="block text-sm font-medium mb-2">Email</label>
                  <input
                    type="email"
                    value={estudianteForm.email}
                    onChange={(e) => setEstudianteForm({...estudianteForm, email: e.target.value})}
                    className="w-full px-3 py-2 border rounded focus:ring-2 focus:ring-blue-500 outline-none"
                    required
                  />
                </div>
                <div className="mb-4">
                  <label className="block text-sm font-medium mb-2">Código</label>
                  <input
                    type="text"
                    value={estudianteForm.codigo}
                    onChange={(e) => setEstudianteForm({...estudianteForm, codigo: e.target.value})}
                    className="w-full px-3 py-2 border rounded focus:ring-2 focus:ring-blue-500 outline-none"
                    required
                  />
                </div>
                <div className="flex gap-2">
                  <button
                    type="submit"
                    disabled={loading}
                    className="flex-1 bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 disabled:opacity-50"
                  >
                    {editingEstudiante ? 'Actualizar' : 'Crear'}
                  </button>
                  {editingEstudiante && (
                    <button
                      type="button"
                      onClick={() => {
                        setEditingEstudiante(null);
                        setEstudianteForm({ nombre: '', apellido: '', email: '', codigo: '' });
                      }}
                      className="px-4 py-2 border rounded hover:bg-gray-100"
                    >
                      Cancelar
                    </button>
                  )}
                </div>
              </form>
            </div>

            <div className="bg-white p-6 rounded-lg shadow">
              <h2 className="text-xl font-bold mb-4">Lista de Estudiantes</h2>
              <div className="space-y-2 max-h-96 overflow-y-auto">
                {estudiantes.map(estudiante => (
                  <div key={estudiante.id} className="p-4 border rounded hover:bg-gray-50">
                    <div className="flex justify-between items-start">
                      <div className="flex-1">
                        <h3 className="font-semibold">{estudiante.nombre} {estudiante.apellido}</h3>
                        <p className="text-sm text-gray-600">{estudiante.email}</p>
                        <p className="text-sm text-gray-600">Código: {estudiante.codigo}</p>
                      </div>
                      <div className="flex gap-2">
                        <button
                          onClick={() => editarEstudiante(estudiante)}
                          className="p-2 text-yellow-600 hover:bg-yellow-50 rounded"
                        >
                          <Edit2 size={18} />
                        </button>
                        <button
                          onClick={() => eliminarEstudiante(estudiante.id)}
                          className="p-2 text-red-600 hover:bg-red-50 rounded"
                        >
                          <Trash2 size={18} />
                        </button>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>
        )}

        {activeTab === 'inscripciones' && (
          <div className="max-w-2xl mx-auto bg-white p-6 rounded-lg shadow">
            <h2 className="text-xl font-bold mb-4">Inscribir Estudiante en Materia</h2>
            <form onSubmit={inscribirEstudiante}>
              <div className="mb-4">
                <label className="block text-sm font-medium mb-2">Estudiante</label>
                <select
                  value={inscripcionForm.estudianteId}
                  onChange={(e) => setInscripcionForm({...inscripcionForm, estudianteId: e.target.value})}
                  className="w-full px-3 py-2 border rounded focus:ring-2 focus:ring-blue-500 outline-none"
                  required
                >
                  <option value="">Seleccione un estudiante</option>
                  {estudiantes.map(est => (
                    <option key={est.id} value={est.id}>
                      {est.nombre} {est.apellido} - {est.codigo}
                    </option>
                  ))}
                </select>
              </div>
              <div className="mb-4">
                <label className="block text-sm font-medium mb-2">Materia</label>
                <select
                  value={inscripcionForm.materiaId}
                  onChange={(e) => setInscripcionForm({...inscripcionForm, materiaId: e.target.value})}
                  className="w-full px-3 py-2 border rounded focus:ring-2 focus:ring-blue-500 outline-none"
                  required
                >
                  <option value="">Seleccione una materia</option>
                  {materias.map(mat => (
                    <option key={mat.id} value={mat.id}>
                      {mat.nombre} ({mat.creditos} créditos)
                    </option>
                  ))}
                </select>
              </div>
              <button
                type="submit"
                disabled={loading}
                className="w-full bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 disabled:opacity-50"
              >
                Inscribir
              </button>
            </form>
          </div>
        )}

        {activeTab === 'notas' && (
          <div>
            {!selectedEstudiante ? (
              <div className="bg-white p-8 rounded-lg shadow text-center">
                <p className="text-gray-600 mb-4">
                  Para gestionar notas, primero seleccione un estudiante desde la pestaña de Materias
                </p>
                <button
                  onClick={() => setActiveTab('materias')}
                  className="bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700"
                >
                  Ir a Materias
                </button>
              </div>
            ) : (
              <div className="space-y-6">
                <div className="bg-white p-6 rounded-lg shadow">
                  <h2 className="text-xl font-bold mb-4">Registrar Nueva Nota</h2>
                  <form onSubmit={crearNota}>
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-4">
                      <div>
                        <label className="block text-sm font-medium mb-2">Valor (0.0 - 5.0)</label>
                        <input
                          type="number"
                          step="0.01"
                          min="0"
                          max="5"
                          value={notaForm.valor}
                          onChange={(e) => setNotaForm({...notaForm, valor: e.target.value})}
                          className="w-full px-3 py-2 border rounded focus:ring-2 focus:ring-blue-500 outline-none"
                          required
                        />
                      </div>
                      <div>
                        <label className="block text-sm font-medium mb-2">Porcentaje (%)</label>
                        <input
                          type="number"
                          min="1"
                          max="100"
                          value={notaForm.porcentaje}
                          onChange={(e) => setNotaForm({...notaForm, porcentaje: e.target.value})}
                          className="w-full px-3 py-2 border rounded focus:ring-2 focus:ring-blue-500 outline-none"
                          required
                        />
                      </div>
                      <div>
                        <label className="block text-sm font-medium mb-2">Estudiante-Materia ID</label>
                        <input
                          type="number"
                          value={notaForm.estudianteMateriaId}
                          onChange={(e) => setNotaForm({...notaForm, estudianteMateriaId: e.target.value})}
                          className="w-full px-3 py-2 border rounded focus:ring-2 focus:ring-blue-500 outline-none"
                          required
                        />
                      </div>
                    </div>
                    <div className="mb-4">
                      <label className="block text-sm font-medium mb-2">Descripción</label>
                      <input
                        type="text"
                        value={notaForm.descripcion}
                        onChange={(e) => setNotaForm({...notaForm, descripcion: e.target.value})}
                        className="w-full px-3 py-2 border rounded focus:ring-2 focus:ring-blue-500 outline-none"
                      />
                    </div>
                    <button
                      type="submit"
                      disabled={loading}
                      className="bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700 disabled:opacity-50"
                    >
                      Registrar Nota
                    </button>
                  </form>
                </div>

                {notaAcumulada && (
                  <div className="bg-white p-6 rounded-lg shadow">
                    <h2 className="text-xl font-bold mb-4">Resumen de Notas</h2>
                    
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
                      <div className="bg-blue-50 p-4 rounded">
                        <p className="text-sm text-gray-600">Nota Acumulada</p>
                        <p className="text-2xl font-bold text-blue-600">{notaAcumulada.notaAcumulada}</p>
                      </div>
                      <div className="bg-green-50 p-4 rounded">
                        <p className="text-sm text-gray-600">Porcentaje Acumulado</p>
                        <p className="text-2xl font-bold text-green-600">{notaAcumulada.porcentajeAcumulado}%</p>
                      </div>
                      <div className="bg-purple-50 p-4 rounded">
                        <p className="text-sm text-gray-600">Nota Final Proyectada</p>
                        <p className="text-2xl font-bold text-purple-600">{notaAcumulada.notaFinalProyectada}</p>
                      </div>
                    </div>

                    <div className="space-y-2">
                      <h3 className="font-semibold mb-3">Detalle de Notas</h3>
                      {notaAcumulada.notas?.map(nota => (
                        <div key={nota.id} className="p-4 border rounded hover:bg-gray-50">
                          <div className="flex justify-between items-start">
                            <div className="flex-1">
                              <div className="flex items-center gap-3">
                                <span className="text-2xl font-bold text-blue-600">{nota.valor}</span>
                                <div>
                                  <p className="font-medium">{nota.descripcion || 'Sin descripción'}</p>
                                  <p className="text-sm text-gray-600">Porcentaje: {nota.porcentaje}%</p>
                                </div>
                              </div>
                            </div>
                            <button
                              onClick={() => eliminarNota(nota.id)}
                              className="p-2 text-red-600 hover:bg-red-50 rounded"
                            >
                              <Trash2 size={18} />
                            </button>
                          </div>
                        </div>
                      ))}
                    </div>
                  </div>
                )}
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default App;