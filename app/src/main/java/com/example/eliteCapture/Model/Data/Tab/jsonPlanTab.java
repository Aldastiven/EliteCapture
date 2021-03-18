package com.example.eliteCapture.Model.Data.Tab;

import java.util.List;

public class jsonPlanTab {
    String finca;
    List<bloqueNavtab> Bloques;

    public jsonPlanTab(String finca, List<bloqueNavtab> bloques) {
        this.finca = finca;
        Bloques = bloques;
    }

    public String getFinca() {
        return finca;
    }

    public List<bloqueNavtab> getBloques() {
        return Bloques;
    }

    public class bloqueNavtab{
        int idbloque;
        String nombrebloque;
        List<navesNavTab> Naves;

        public bloqueNavtab(int idbloque, String nombrebloque, List<navesNavTab> naves) {
            this.idbloque = idbloque;
            this.nombrebloque = nombrebloque;
            Naves = naves;
        }

        public int getIdbloque() {
            return idbloque;
        }

        public String getNombrebloque() {
            return nombrebloque;
        }

        public List<navesNavTab> getNaves() {
            return Naves;
        }

        public class navesNavTab{
            int idnave;
            int nave;
            List<camasNavTab> Camas;

            public navesNavTab(int idnave, int nave, List<camasNavTab> camas) {
                this.idnave = idnave;
                this.nave = nave;
                Camas = camas;
            }

            public int getIdnave() {
                return idnave;
            }

            public int getNave() {
                return nave;
            }

            public List<camasNavTab> getCamas() {
                return Camas;
            }

            public class camasNavTab{
                int IDCama;
                int Bloque;
                int Cama;
                String Finca;
                String Sufijo;
                String producto;
                String Variedad;
                String AreaNeta;
                String LargoCama;
                int Semana_Siembra;
                int Año_Siembra;
                int Año_Poda;
                int Semana_Poda;
                int CantidadPlantas;
                String TipoSiembra;
                String AnchoCama;
                String Proveedor;
                String TipoIndice;

                public camasNavTab(int IDCama, int bloque, int cama, String finca, String sufijo, String producto, String variedad, String areaNeta, String largoCama, int semana_Siembra, int año_Siembra, int año_Poda, int semana_Poda, int cantidadPlantas, String tipoSiembra, String anchoCama, String proveedor, String tipoIndice) {
                    this.IDCama = IDCama;
                    Bloque = bloque;
                    Cama = cama;
                    Finca = finca;
                    Sufijo = sufijo;
                    this.producto = producto;
                    Variedad = variedad;
                    AreaNeta = areaNeta;
                    LargoCama = largoCama;
                    Semana_Siembra = semana_Siembra;
                    Año_Siembra = año_Siembra;
                    Año_Poda = año_Poda;
                    Semana_Poda = semana_Poda;
                    CantidadPlantas = cantidadPlantas;
                    TipoSiembra = tipoSiembra;
                    AnchoCama = anchoCama;
                    Proveedor = proveedor;
                    TipoIndice = tipoIndice;
                }

                public int getIDCama() {
                    return IDCama;
                }

                public int getBloque() {
                    return Bloque;
                }

                public int getCama() {
                    return Cama;
                }

                public String getFinca() {
                    return Finca;
                }

                public String getSufijo() {
                    return Sufijo;
                }

                public String getProducto() {
                    return producto;
                }

                public String getVariedad() {
                    return Variedad;
                }

                public String getAreaNeta() {
                    return AreaNeta;
                }

                public String getLargoCama() {
                    return LargoCama;
                }

                public int getSemana_Siembra() {
                    return Semana_Siembra;
                }

                public int getAño_Siembra() {
                    return Año_Siembra;
                }

                public int getAño_Poda() {
                    return Año_Poda;
                }

                public int getSemana_Poda() {
                    return Semana_Poda;
                }

                public int getCantidadPlantas() {
                    return CantidadPlantas;
                }

                public String getTipoSiembra() {
                    return TipoSiembra;
                }

                public String getAnchoCama() {
                    return AnchoCama;
                }

                public String getProveedor() {
                    return Proveedor;
                }

                public String getTipoIndice() {
                    return TipoIndice;
                }
            }
        }
    }
}

