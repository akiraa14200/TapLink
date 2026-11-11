-- TapLink profile schema
-- Applied to Supabase project gecnjubgfmgiejdmqhhp on 2026-11-11.
-- This is a schema snapshot, not a Supabase CLI migration.

create table if not exists public.profiles (
  id uuid primary key references auth.users(id) on delete cascade,
  username text not null,
  display_name text not null default '',
  bio text not null default '',
  avatar_url text,
  instagram_url text,
  youtube_url text,
  website_url text,
  is_published boolean not null default false,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  constraint profiles_username_format check (
    username = lower(username)
    and username ~ '^[a-z0-9._]{3,30}$'
  )
);

create unique index if not exists profiles_username_lower_unique
  on public.profiles (lower(username));

alter table public.profiles enable row level security;

revoke all on table public.profiles from anon, authenticated;
grant select on table public.profiles to anon, authenticated;
grant insert, update on table public.profiles to authenticated;

drop policy if exists "Public can read published profiles" on public.profiles;
create policy "Public can read published profiles"
  on public.profiles for select to anon
  using (is_published = true);

drop policy if exists "Users can read published profiles and their own profile" on public.profiles;
create policy "Users can read published profiles and their own profile"
  on public.profiles for select to authenticated
  using (is_published = true or (select auth.uid()) = id);

drop policy if exists "Users can create their own profile" on public.profiles;
create policy "Users can create their own profile"
  on public.profiles for insert to authenticated
  with check ((select auth.uid()) = id);

drop policy if exists "Users can update their own profile" on public.profiles;
create policy "Users can update their own profile"
  on public.profiles for update to authenticated
  using ((select auth.uid()) = id)
  with check ((select auth.uid()) = id);
