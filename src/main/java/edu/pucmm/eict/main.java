package edu.pucmm.eict;

import edu.pucmm.eict.Servicios.ComentariosServices;
import edu.pucmm.eict.Servicios.FotoServices;
import edu.pucmm.eict.Servicios.ProductosServices;
import edu.pucmm.eict.Servicios.VentasServices;
import edu.pucmm.eict.entidades.Comentarios;
import edu.pucmm.eict.entidades.FotosEntidad;
import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class main {

    public static void main(String[] args){

        Tienda controllerProducts = new Tienda();

        CarroCompra controllerCarro = new CarroCompra();

        /////AGREGANDO PRODUCTOS/////////

        Producto productoA = new Producto( "cocacola", 30, 10);
        Producto productoB = new Producto( "papita", 35, 20);
        Producto productoC= new Producto( "chicle", 15, 30);

        controllerProducts.agregarProductoStock("cocacola", 30, 10);
        controllerProducts.agregarProductoStock("papita", 35, 20);
        controllerProducts.agregarProductoStock("chicle", 15, 30);
        ///////CREANDO UN CARRITO DE COMPRAS//////////

         //Inicializar BDD orm

        ORM.getInstance().startDB();
        //Inicializando javalin

       // ProductosServices.getInstancia().crear(new Productos("producto 1", 30, 10));

        Javalin app = Javalin.create( config -> {
            //set configs
            config.addStaticFiles("/Public");

        }).start(7006);

        JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");
        ///Primera vista de la página

        app.get("/", ctx -> {

            ctx.redirect("/1");
        });

        app.get("/:id", ctx -> {

            String url = ctx.req.getRequestURI();
            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(url);
            int id = 0;
            while(m.find()) {
                if (Integer.parseInt(m.group()) > 0){
                    id = Integer.parseInt(m.group());
                    break;
                }

            };

            System.out.println("ESTE ES EL ID/////////// /////////" + id);

            int cant = 0;
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("paginas", ProductosServices.getInstancia().findAllProducts().size()/10);

                modelo.put("Productos", ProductosServices.getInstancia().findAllProductspaginados(id));


            ctx.sessionAttribute("STOCK", controllerProducts);//ESTA ES LA SESIÓN DEL STOCK DE PRODUCTOS EN TODA LA TIENDA


            if (ctx.sessionAttribute("carritoCreado") != null){

                CarroCompra carrito = ctx.sessionAttribute("carritoCreado");

                for (Producto prod:
                        carrito.getListaProductos()) {

                    //////////////


                    cant += prod.getQuantity();
                }
            } else {
                cant = 0;
            }
            ctx.sessionAttribute("CANTI", cant);
            System.out.println("LA CANTIDAD ES "+ cant);
            modelo.put("CANTIDAD",cant);
            ctx.render("/Templates/ListadoProductos.html", modelo);



        });
        /////////////////////////////////////////////////////////////////////////////////////////////////////////





        app.get("/Autenticar/*", ctx -> {

            if (ctx.sessionAttribute("autenticado") == null){

                ctx.render("/Templates/Login.html");

            } else{







                String url = ctx.req.getRequestURI();

                if(url.equalsIgnoreCase("/Autenticar/ventas")){
                    /*
                    CarroCompra carroUser = ctx.sessionAttribute("carritoCreado");
                    Date date = new Date();
                    VentasProductos ventas = new VentasProductos(date, ctx.sessionAttribute("COMPRADOR"),  carroUser);
                    Tienda controller = ctx.sessionAttribute("STOCK");
                    controller.agregarVentas(ventas);*/



                    HashMap<String, Object> model = new HashMap<>();
                    model.put("ventas", VentasServices.getInstancia().findAllVentas());
                    model.put("cantidad", ctx.sessionAttribute("CANTI"));
                    ctx.render("/Templates/Ventas.html", model);



                };
                if(url.equalsIgnoreCase("/Autenticar/productos")){
                   // Tienda stock2 = ctx.sessionAttribute("STOCK");
                   // ArrayList<Producto> stock = stock2.getProductosController();
                    HashMap<String, Object> model = new HashMap<>();

                    model.put("stock", ProductosServices.getInstancia().findAllProducts());

                    model.put("cantidad", ctx.sessionAttribute("CANTI"));

                    ctx.render("/Templates/CrudProductos.html", model);

                };





                //System.out.printf("\nYA HABIAS INICIADO SESION PAPUS\n Este es ese código dique URL: " + ctx.req.getRequestURI());
                //AQUI VA LA MANIOBRA PARA VOLVER A LA PAGINA ANTERIOR (WORKING ON IT)







            };
        });

        app.post("/ProcesarCompra", ctx -> {

            String nombre = ctx.formParam("nombreUser");

            ctx.sessionAttribute("COMPRADOR",nombre);



            CarroCompra carroUser = ctx.sessionAttribute("carritoCreado"); //el arraylist de productos comprados en el carrito

            Date date = new Date();



            String nombreProd = "nada";
            float precioProd = 1;
            Integer cant = 1;


            List<Producto> setProductos = new ArrayList<>();

            for(int i = 0; i < carroUser.getListaProductos().size(); i++){

                nombreProd = carroUser.getListaProductos().get(i).getNombre();
                precioProd = carroUser.getListaProductos().get(i).getPrecio();
                cant = carroUser.getListaProductos().get(i).getQuantity();

                Producto prod = new Producto(nombreProd, precioProd, cant);
                prod.setId(carroUser.getListaProductos().get(i).getId());
                setProductos.add(prod);
                System.out.println("ID DEL PRODUCTO ES//////////////////:  "+ carroUser.getListaProductos().get(i).getId());

            }



           VentasProductos ventasX = new VentasProductos(date, nombre, setProductos);

            VentasServices.getInstancia().crear(ventasX);

           // VentasProductos ventas = new VentasProductos(date, ctx.sessionAttribute("COMPRADOR"),  setProductos);

           // controllerProducts.agregarVentas(ventas);

            ctx.sessionAttribute("carritoCreado", null);
            ctx.redirect("/");

        });

        app.post("/CrearProducto", ctx -> {

            Producto prod = new Producto("", 0, 99, "");
            prod.setId(0);
            HashMap<String, Object> map = new HashMap<>();

            map.put("modificable",prod);
            ctx.render("/Templates/CrearProducto.html", map);
            //ctx.redirect("");




        });

        app.get("/Modificar/mod/*", ctx -> {

            String url = ctx.req.getRequestURI();

           // String nombre = ctx.pathParam("nombre");

          //  float precio = Float.parseFloat(ctx.pathParam("precio"));

            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(url);
            int id = 0;
            while(m.find()) {
                id = Integer.parseInt(m.group());
            };

            HashMap<String, Object> map = new HashMap<>();
            map.put("modificable",ProductosServices.getInstancia().find(id));
            ctx.render("/Templates/CrearProducto.html", map);

            System.out.println("ESTE ES EL ID "+id);

          //  Productos producto = new Productos("1", 1, 99);
           // producto.setId(id);
           // producto.setPrecio(precio);
           // producto.setNombre(nombre);

           // ProductosServices.getInstancia().editar(producto);

            /*HashMap<String, Object> map1 = new HashMap<>();
            map1.put("modificable",ProductosServices.getInstancia().findAllProducts());
            ctx.render("/Templates/CrearProducto.html", map1);*/


        });

        app.get("/Modificar/borrar/*", ctx -> {
            String url = ctx.req.getRequestURI();


            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(url);
            int id = 0;
            while(m.find()) {
                id = Integer.parseInt(m.group());
            };

            FotoServices.getInstancia().deleteFoto(id);
            HashMap<String, Object> map = new HashMap<>();
            map.put("stock",ProductosServices.getInstancia().findAllProducts());
            ctx.render("/Templates/CrudProductos.html", map);

        });


        app.post("/Crear" , ctx -> {


           // Tienda stock = ctx.sessionAttribute("STOCK");
            String nombre = ctx.formParam("nombreNuevo");
            System.out.println("VALOR DEL NOMBREEEEE " + nombre);
            Float precio = Float.parseFloat(ctx.formParam("precioNuevo"));
            String desc = ctx.formParam("descripcion");
            int cant = 99;
            int id = Integer.parseInt(ctx.formParam("idNuevo"));
            Producto productoExistente = ProductosServices.getInstancia().find(id);

            if(productoExistente != null){ //PARA MODIFICAR

                //System.out.println("ESTAMOS DENTRO KLK");
                Producto producto = new Producto(nombre, precio, cant, desc);
                producto.setId(id);
                ProductosServices.getInstancia().editar(producto);

                if (!ctx.uploadedFiles("foto").isEmpty()){

                    ctx.uploadedFiles("foto").forEach(uploadedFile -> {
                        try {
                            byte[] bytes = uploadedFile.getContent().readAllBytes();
                            String encodedString = Base64.getEncoder().encodeToString(bytes);
                            FotosEntidad foto = new FotosEntidad(uploadedFile.getFilename(), uploadedFile.getContentType(),
                                    encodedString, producto);
                            FotoServices.getInstancia().crear(foto);
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    });
                }else {
                    ctx.uploadedFiles("foto2").forEach(uploadedFile -> {
                        try {
                            byte[] bytes = uploadedFile.getContent().readAllBytes();
                            String encodedString = Base64.getEncoder().encodeToString(bytes);
                            FotosEntidad foto = new FotosEntidad(uploadedFile.getFilename(), uploadedFile.getContentType(),
                                    encodedString, producto);
                            FotoServices.getInstancia().crear(foto);
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    });
                }


               // Producto prod = stock.prodByID(id);
               // prod.setNombre(nombre);
              //  prod.setPrecio(precio);
              //  prod.setQuantity(99);

                //Ingresar producto creado a la base de datos
                /*
                Productos prodEntity = new Productos(nombre, precio, cant);
                ProductosServices.getInstancia().editar(prodEntity);
                ProductosServices.getInstancia().crear(new Productos(nombre, precio, cant));*/

            }else {//PARA CREAR
               // stock.agregarProductoStock(nombre,precio,cant);
                Producto producto = new Producto(nombre, precio, cant, desc);
                ProductosServices.getInstancia().crear(producto);
                if (!ctx.uploadedFiles("foto").isEmpty()){

                    ctx.uploadedFiles("foto").forEach(uploadedFile -> {
                        try {
                            byte[] bytes = uploadedFile.getContent().readAllBytes();
                            String encodedString = Base64.getEncoder().encodeToString(bytes);
                            FotosEntidad foto = new FotosEntidad(uploadedFile.getFilename(), uploadedFile.getContentType(),
                                    encodedString, producto);
                            FotoServices.getInstancia().crear(foto);
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    });
                }else {
                    ctx.uploadedFiles("foto2").forEach(uploadedFile -> {
                        try {
                            byte[] bytes = uploadedFile.getContent().readAllBytes();
                            String encodedString = Base64.getEncoder().encodeToString(bytes);
                            FotosEntidad foto = new FotosEntidad(uploadedFile.getFilename(), uploadedFile.getContentType(),
                                    encodedString, producto);
                            FotoServices.getInstancia().crear(foto);
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    });
                }
            }

            HashMap<String, Object> model = new HashMap<>();

            model.put("stock", ProductosServices.getInstancia().findAllProducts());

            ctx.render("/Templates/CrudProductos.html", model);




        });




        app.post("/Iniciarsesion", ctx -> {

            String nombre = ctx.formParam("USERNAME");
            String password = ctx.formParam("PASSWORD");
            System.out.println("ESTOS SON LOS PARAMETROS INGRESADOS: "+nombre+"   "+password);

            if (nombre.contentEquals("admin") & password.contentEquals("admin") ){

                System.out.printf("\nFELICIDADES, INICIASTE SESIÓN BRODEL\n");
                Usuario user = new Usuario("admin", "jhan", "admin");

                ctx.sessionAttribute("autenticado",user);
                ctx.redirect("/");



            };


        });













        /////////////////////////////////////////////////////////////////////////////////////////////////////////


        app.get("/carrito", ctx -> {

            if(ctx.sessionAttribute("carritoCreado") == null){
                CarroCompra carroUser = new CarroCompra();
                ctx.sessionAttribute("carritoCreado", carroUser);
                System.out.printf(" CREADA A SESIÓN DEL CARRITO");
                ctx.render("Templates/Carrito_Compra.html");
            } else {
                CarroCompra miCarrito = ctx.sessionAttribute("carritoCreado");

                HashMap<String, Object> mapaCarrito = new HashMap<>();
                mapaCarrito.put("miCarrito", miCarrito.getListaProductos());
                mapaCarrito.put("cantidad", ctx.sessionAttribute("CANTI"));
                ctx.render("Templates/Carrito_Compra.html", mapaCarrito);
            }






        });

        app.post("/eliminar", ctx -> {
            CarroCompra miCarrito = ctx.sessionAttribute("carritoCreado");
            int idd = Integer.parseInt(ctx.formParam("productoID"));

            for (Producto prod:
                    miCarrito.getListaProductos()) {



                if((prod.getId() == Integer.parseInt(ctx.formParam("productoID")) && ctx.formParam("nombreProducto").equalsIgnoreCase(prod.getNombre())))
                {
                    System.out.println("PRODUCTO BORRADO");
                    System.out.println(prod.getId());
                    miCarrito.eliminarProductoCarrito(prod.getId());
                    break;
                }
            }

            HashMap<String, Object> modelo = new HashMap<>();
            modelo.put("miCarrito", miCarrito.getListaProductos());



            ctx.render("/Templates/Carrito_Compra.html", modelo);

        });

        app.post("/Agregar", ctx -> {

            String nombreProd = ctx.formParam("nombreProd");

            float precioProd = Float.parseFloat(ctx.formParam("precioProd").toString());

            int cantProd = Integer.parseInt(ctx.formParam("cantProd").toString());


           int id = Integer.parseInt(ctx.formParam("idProducto" ));



            //Crear la sesión pal carrito y verificación

            if(ctx.sessionAttribute("carritoCreado") == null){
                CarroCompra carroUser = new CarroCompra();
                carroUser.agregarProductoCarrito(id,nombreProd,precioProd,cantProd);

                ctx.sessionAttribute("carritoCreado", carroUser); //CREADA LA SESIÓN
                System.out.printf(" CREADA A SESIÓN DEL CARRITO");
            } else {

                CarroCompra carritoExistente = ctx.sessionAttribute("carritoCreado");
                carritoExistente.agregarProductoCarrito(id,nombreProd,precioProd,cantProd);

            }
            ctx.redirect("/");

        });

        app.get("/LimpiarCarro", ctx -> {

            CarroCompra carroUser = new CarroCompra();
            carroUser = ctx.sessionAttribute("carritoCreado");
            carroUser.limpiarCarrito();

            System.out.println("CARRITO VACIADO CON EXITO MIOP JEJE ADIOS COMPRA");

            ctx.render("/Templates/Carrito_Compra.html");



        });


        app.get("/comment/*", ctx -> {
            String url = ctx.req.getRequestURI();


            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(url);
            int id = 0;
            while(m.find()) {
                if (Integer.parseInt(m.group()) > 0){
                    id = Integer.parseInt(m.group());
                    break;
                }

            };

            HashMap<String, Object> model = new HashMap<>();
            if(id > 0){
                model.put("productoID", id);

                System.out.println("ID MANDADO AL MODEL ///////////////////" +id);
                ctx.render("/Templates/Comentarios.html", model);

            }


        });

        app.post("/comentar", ctx -> {

            int id = 0001;

            String nombreUser = ctx.formParam("userComentario");
            String comentario = ctx.formParam("comentario");
            id = Integer.parseInt(ctx.formParam("idProductoComentado"));
            Comentarios comment = new Comentarios(nombreUser, comentario, ProductosServices.getInstancia().find(id));
            ComentariosServices.getInstancia().crear(comment);

            ctx.redirect("/");


        });

        app.get("/delete/*", ctx -> {

            String url = ctx.req.getRequestURI();

            Producto prod = new Producto();

            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(url);
            int id = 0;
            while(m.find()) {
                if (Integer.parseInt(m.group()) > 0){
                    id = Integer.parseInt(m.group());
                    break;
                }

            };

            if(id > 0){
                prod = ProductosServices.getInstancia().find(id);
            }

            HashMap<String, Object> map = new HashMap<>();

            map.put("comentarios", prod.getProductoComment());

            ctx.render("/Templates/ManageComments.html", map);


        });

        app.post("/borrarComment", ctx -> {
           int id = Integer.parseInt(ctx.formParam("ComentarioID"));
            ComentariosServices.getInstancia().eliminar(id);

           ctx.redirect("/Autenticar/productos");


        });



    }

}

