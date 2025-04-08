// Interface para clareza (pode ser movida para um arquivo de models se preferir)
export interface CursoOpcao {
    value: string;    // Valor armazenado/enviado (ex: MEDICINA_VETERINARIA)
    display: string; // Texto a ser exibido (ex: Medicina Veterinária)
  }
  
  // A lista de cursos como fonte única da verdade
  export const CURSOS_LISTA: ReadonlyArray<CursoOpcao> = [
    { value: "BIOLOGIA", display: "Biologia" },
    { value: "BIOMEDICINA", display: "Biomedicina" },
    { value: "ENFERMAGEM", display: "Enfermagem" },
    { value: "FARMACIA", display: "Farmácia" },
    { value: "FISIOTERAPIA", display: "Fisioterapia" },
    { value: "FONOAUDIOLOGIA", display: "Fonoaudiologia" },
    { value: "MEDICINA", display: "Medicina" },
    { value: "MEDICINA_VETERINARIA", display: "Medicina Veterinária" },
    { value: "NUTRICAO", display: "Nutrição" },
    { value: "ODONTOLOGIA", display: "Odontologia" },
    { value: "PSICOLOGIA", display: "Psicologia" },
    { value: "TERAPIA_OCUPACIONAL", display: "Terapia Ocupacional" }
  ] as const; // 'as const' torna o array e seus objetos imutáveis
  
  // Um mapa gerado a partir da lista para facilitar a busca do display pelo value
  // Usamos Record<string, string> para tipar um objeto chave-valor de strings
  export const CURSOS_MAP: Readonly<Record<string, string>> =
    Object.fromEntries(
      CURSOS_LISTA.map(curso => [curso.value, curso.display])
    );
  
  /*
  // Alternativa para gerar o mapa se Object.fromEntries não for suportado (browsers antigos)
  const map: { [key: string]: string } = {};
  for (const curso of CURSOS_LISTA) {
    map[curso.value] = curso.display;
  }
  export const CURSOS_MAP: Readonly<Record<string, string>> = map;
  */
  