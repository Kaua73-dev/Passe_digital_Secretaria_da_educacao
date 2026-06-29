const form = document.getElementById('registerForm');
const cancelButton = document.getElementById('cancelButton');
const submitButton = document.getElementById('submitButton');
const formMessage = document.getElementById('formMessage');

const fields = {
  name: document.getElementById('name'),
  email: document.getElementById('email'),
  registration: document.getElementById('registration'),
  password: document.getElementById('password'),
  studentClass: document.getElementById('studentClass'),
  studentEducation: document.getElementById('studentEducation'),
  birth: document.getElementById('birth'),
  shift: document.getElementById('shift'),
};

const errors = {
  name: document.getElementById('nameError'),
  email: document.getElementById('emailError'),
  registration: document.getElementById('registrationError'),
  password: document.getElementById('passwordError'),
  studentClass: document.getElementById('studentClassError'),
  studentEducation: document.getElementById('studentEducationError'),
  birth: document.getElementById('birthError'),
  shift: document.getElementById('shiftError'),
};

const validateEmail = (value) => {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);
};

const resetErrors = () => {
  Object.values(errors).forEach((errorElement) => {
    errorElement.textContent = '';
  });
  formMessage.textContent = '';
  formMessage.className = '';
};

const validateForm = () => {
  resetErrors();

  let isValid = true;

  if (!fields.name.value.trim()) {
    errors.name.textContent = 'Nome obrigatório';
    isValid = false;
  }

  if (!fields.email.value.trim()) {
    errors.email.textContent = 'Email obrigatório';
    isValid = false;
  } else if (!validateEmail(fields.email.value)) {
    errors.email.textContent = 'Email inválido';
    isValid = false;
  }

  if (!fields.registration.value.trim()) {
    errors.registration.textContent = 'Matrícula obrigatória';
    isValid = false;
  }

  if (!fields.password.value.trim()) {
    errors.password.textContent = 'Senha obrigatória';
    isValid = false;
  }

  if (!fields.studentClass.value.trim()) {
    errors.studentClass.textContent = 'Turma obrigatória';
    isValid = false;
  }

  if (!fields.studentEducation.value) {
    errors.studentEducation.textContent = 'Nível de ensino obrigatório';
    isValid = false;
  }

  if (!fields.birth.value) {
    errors.birth.textContent = 'Data de nascimento obrigatória';
    isValid = false;
  }

  if (!fields.shift.value) {
    errors.shift.textContent = 'Turno obrigatório';
    isValid = false;
  }

  return isValid;
};

const setLoading = (isLoading) => {
  submitButton.disabled = isLoading;
  submitButton.textContent = isLoading ? 'Cadastrando...' : 'Cadastrar aluno';
};

const buildPayload = () => {
  return {
    name: fields.name.value.trim(),
    email: fields.email.value.trim(),
    registration: fields.registration.value.trim(),
    password: fields.password.value,
    studentClass: { studentClass: fields.studentClass.value.trim() },
    studentEducation: { studentEducation: fields.studentEducation.value },
    birth: fields.birth.value,
    userStudentShiftEnum: fields.shift.value,
  };
};

const clearForm = () => {
  form.reset();
  resetErrors();
};

const showMessage = (message, type = 'success') => {
  formMessage.textContent = message;
  formMessage.className = type === 'success' ? 'text-green-600' : 'text-red-600';
};

const handleSubmit = async (event) => {
  event.preventDefault();

  if (!validateForm()) {
    return;
  }

  setLoading(true);
  showMessage('', '');

  try {
    const payload = buildPayload();
    const response = await Auth.fetchWithAuth('/secretary/student/register', {
      method: 'POST',
      body: JSON.stringify(payload),
    });

    const data = await response.json().catch(() => null);

    if (!response.ok) {
      const errorMessage = data?.message || 'Não foi possível cadastrar o aluno. Tente novamente.';
      showMessage(errorMessage, 'error');
      return;
    }

    showMessage('Aluno cadastrado com sucesso.', 'success');
    clearForm();
  } catch (error) {
    showMessage(error.message || 'Erro inesperado ao cadastrar o aluno.', 'error');
  } finally {
    setLoading(false);
  }
};

const handleCancel = () => {
  window.location.href = '/frontend/html/app.html';
};

const ensureAccess = () => {
  Auth.requireRole(['ADMIN', 'SECRETARY']);
};

document.addEventListener('DOMContentLoaded', () => {
  ensureAccess();
  form.addEventListener('submit', handleSubmit);
  cancelButton.addEventListener('click', handleCancel);
});
